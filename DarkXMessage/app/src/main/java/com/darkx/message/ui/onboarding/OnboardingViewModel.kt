package com.darkx.message.ui.onboarding

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.darkx.message.DarkXApp
import com.darkx.message.core.permissions.DefaultSmsHandler
import com.darkx.message.domain.repository.OnboardingStatusStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class OnboardingStep { Welcome, NativeSms, Notifications, DefaultSms, Done }

data class OnboardingUiState(
    val step: OnboardingStep = OnboardingStep.Welcome,
    val isRoleAvailable: Boolean = true,
    val isDefaultSmsApp: Boolean = false,
    val roleRequestDeclined: Boolean = false,
)

class OnboardingViewModel(
    private val defaultSms: DefaultSmsHandler,
    private val store: OnboardingStatusStore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        OnboardingUiState(
            isRoleAvailable = defaultSms.isRoleAvailable(),
            isDefaultSmsApp = defaultSms.isDefaultSmsApp(),
        ),
    )
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun next() = moveBy(+1)

    fun back() = moveBy(-1)

    fun createRoleRequestIntent(): Intent? = defaultSms.createRequestIntent()

    /** Re-reads role state, e.g. when the user returns from system settings. */
    fun refreshRoleState() {
        val isDefault = defaultSms.isDefaultSmsApp()
        _uiState.update {
            it.copy(
                isRoleAvailable = defaultSms.isRoleAvailable(),
                isDefaultSmsApp = isDefault,
                roleRequestDeclined = it.roleRequestDeclined && !isDefault,
            )
        }
    }

    fun onRoleRequestResult() {
        val isDefault = defaultSms.isDefaultSmsApp()
        _uiState.update {
            if (isDefault) {
                it.copy(isDefaultSmsApp = true, roleRequestDeclined = false, step = OnboardingStep.Done)
            } else {
                it.copy(isDefaultSmsApp = false, roleRequestDeclined = true)
            }
        }
    }

    fun onRoleRequestUnavailable() {
        _uiState.update { it.copy(isRoleAvailable = false) }
    }

    fun finish() = store.markOnboardingCompleted()

    private fun moveBy(delta: Int) {
        _uiState.update {
            val target = (it.step.ordinal + delta).coerceIn(0, OnboardingStep.entries.lastIndex)
            it.copy(step = OnboardingStep.entries[target])
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as DarkXApp
                OnboardingViewModel(app.container.defaultSmsHandler, app.container.preferences)
            }
        }
    }
}
