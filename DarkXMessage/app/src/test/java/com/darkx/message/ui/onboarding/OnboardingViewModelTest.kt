package com.darkx.message.ui.onboarding

import android.content.Intent
import com.darkx.message.core.permissions.DefaultSmsHandler
import com.darkx.message.domain.repository.OnboardingStatusStore
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OnboardingViewModelTest {

    private class FakeDefaultSms(
        var available: Boolean = true,
        var isDefault: Boolean = false,
    ) : DefaultSmsHandler {
        override fun isRoleAvailable() = available
        override fun isDefaultSmsApp() = isDefault
        override fun createRequestIntent(): Intent? = null
    }

    private class FakeStore : OnboardingStatusStore {
        var completed = false
        override fun markOnboardingCompleted() {
            completed = true
        }
    }

    private fun viewModel(
        sms: FakeDefaultSms = FakeDefaultSms(),
        store: FakeStore = FakeStore(),
    ) = OnboardingViewModel(sms, store)

    @Test
    fun `starts on welcome step`() {
        assertEquals(OnboardingStep.Welcome, viewModel().uiState.value.step)
    }

    @Test
    fun `next walks through every step and stops at done`() {
        val vm = viewModel()
        repeat(10) { vm.next() }
        assertEquals(OnboardingStep.Done, vm.uiState.value.step)
    }

    @Test
    fun `back does not go before welcome`() {
        val vm = viewModel()
        vm.back()
        assertEquals(OnboardingStep.Welcome, vm.uiState.value.step)
    }

    @Test
    fun `declined role request is flagged and stays on the step`() {
        val vm = viewModel()
        repeat(3) { vm.next() }
        vm.onRoleRequestResult()
        assertEquals(OnboardingStep.DefaultSms, vm.uiState.value.step)
        assertTrue(vm.uiState.value.roleRequestDeclined)
        assertFalse(vm.uiState.value.isDefaultSmsApp)
    }

    @Test
    fun `granted role request advances to done`() {
        val sms = FakeDefaultSms()
        val vm = viewModel(sms)
        repeat(3) { vm.next() }
        sms.isDefault = true
        vm.onRoleRequestResult()
        assertEquals(OnboardingStep.Done, vm.uiState.value.step)
        assertTrue(vm.uiState.value.isDefaultSmsApp)
        assertFalse(vm.uiState.value.roleRequestDeclined)
    }

    @Test
    fun `refresh clears declined flag once app becomes default`() {
        val sms = FakeDefaultSms()
        val vm = viewModel(sms)
        vm.onRoleRequestResult()
        assertTrue(vm.uiState.value.roleRequestDeclined)
        sms.isDefault = true
        vm.refreshRoleState()
        assertFalse(vm.uiState.value.roleRequestDeclined)
        assertTrue(vm.uiState.value.isDefaultSmsApp)
    }

    @Test
    fun `unavailable role is reflected in state`() {
        val vm = viewModel(FakeDefaultSms(available = false))
        assertFalse(vm.uiState.value.isRoleAvailable)
    }

    @Test
    fun `finish marks onboarding completed`() {
        val store = FakeStore()
        viewModel(store = store).finish()
        assertTrue(store.completed)
    }
}
