package com.darkx.message.ui.onboarding

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darkx.message.ui.components.StepIndicator
import com.darkx.message.ui.theme.Spacing

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = viewModel(factory = OnboardingViewModel.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // The user may change the default app from system settings and come back.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.refreshRoleState()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    BackHandler(enabled = state.step != OnboardingStep.Welcome) { viewModel.back() }

    // Denial is not fatal: the app stays usable and notifications can be enabled later.
    val notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { viewModel.next() }

    val roleLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { viewModel.onRoleRequestResult() }

    fun requestNotifications() {
        val needsRuntimePermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        if (needsRuntimePermission) {
            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            viewModel.next()
        }
    }

    fun requestDefaultSmsRole() {
        val intent = viewModel.createRoleRequestIntent()
        if (intent == null) {
            viewModel.onRoleRequestUnavailable()
            return
        }
        try {
            roleLauncher.launch(intent)
        } catch (_: ActivityNotFoundException) {
            viewModel.onRoleRequestUnavailable()
        }
    }

    fun openDefaultAppSettings() {
        try {
            context.startActivity(Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS))
        } catch (_: ActivityNotFoundException) {
            try {
                context.startActivity(Intent(Settings.ACTION_SETTINGS))
            } catch (_: ActivityNotFoundException) {
                // No settings app available; the on-screen explanation remains.
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = Spacing.lg, vertical = Spacing.md),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StepIndicator(current = state.step.ordinal, total = OnboardingStep.entries.size)
        Spacer(Modifier.height(Spacing.md))

        AnimatedContent(
            targetState = state.step,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            transitionSpec = {
                val forward = targetState.ordinal > initialState.ordinal
                val direction = if (forward) 1 else -1
                (slideInHorizontally(tween(260)) { direction * it / 4 } + fadeIn(tween(220))) togetherWith
                    (slideOutHorizontally(tween(200)) { -direction * it / 4 } + fadeOut(tween(160)))
            },
            label = "onboardingStep",
        ) { step ->
            when (step) {
                OnboardingStep.Welcome -> WelcomePage(onContinue = viewModel::next)
                OnboardingStep.NativeSms -> NativeSmsPage(onContinue = viewModel::next)
                OnboardingStep.Notifications -> NotificationsPage(
                    onAllow = ::requestNotifications,
                    onSkip = viewModel::next,
                )
                OnboardingStep.DefaultSms -> DefaultSmsPage(
                    state = state,
                    onSetDefault = ::requestDefaultSmsRole,
                    onOpenSettings = ::openDefaultAppSettings,
                    onContinue = viewModel::next,
                )
                OnboardingStep.Done -> DonePage(
                    isDefaultSmsApp = state.isDefaultSmsApp,
                    onFinish = {
                        viewModel.finish()
                        onFinished()
                    },
                )
            }
        }
    }
}
