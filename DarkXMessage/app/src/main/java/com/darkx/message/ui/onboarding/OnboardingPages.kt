package com.darkx.message.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.darkx.message.R
import com.darkx.message.ui.components.DarkXLogo
import com.darkx.message.ui.components.DarkXPrimaryButton
import com.darkx.message.ui.components.DarkXTextButton
import com.darkx.message.ui.theme.Spacing

@Composable
private fun OnboardingPage(
    hero: @Composable () -> Unit,
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit = {},
    actions: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            hero()
            Spacer(Modifier.height(Spacing.lg))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(Spacing.sm + Spacing.xs))
            Text(
                text = body,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(Spacing.md))
            content()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = actions,
        )
    }
}

@Composable
private fun HeroBadge(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(96.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(44.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
private fun FeaturePoint(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.xs),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(20.dp),
            tint = MaterialTheme.colorScheme.tertiary,
        )
        Spacer(Modifier.size(Spacing.sm + Spacing.xs))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun InfoCard(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(Spacing.md),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
internal fun WelcomePage(onContinue: () -> Unit) {
    OnboardingPage(
        hero = { DarkXLogo(size = 112.dp) },
        title = stringResource(R.string.onboarding_welcome_title),
        body = stringResource(R.string.onboarding_welcome_body),
    ) {
        DarkXPrimaryButton(stringResource(R.string.onboarding_continue), onContinue)
    }
}

@Composable
internal fun NativeSmsPage(onContinue: () -> Unit) {
    OnboardingPage(
        hero = { HeroBadge(Icons.AutoMirrored.Filled.Send) },
        title = stringResource(R.string.onboarding_sms_title),
        body = stringResource(R.string.onboarding_sms_body),
        content = {
            FeaturePoint(stringResource(R.string.onboarding_sms_point_carrier))
            FeaturePoint(stringResource(R.string.onboarding_sms_point_private))
            FeaturePoint(stringResource(R.string.onboarding_sms_point_offline))
        },
    ) {
        DarkXPrimaryButton(stringResource(R.string.onboarding_continue), onContinue)
    }
}

@Composable
internal fun NotificationsPage(onAllow: () -> Unit, onSkip: () -> Unit) {
    OnboardingPage(
        hero = { HeroBadge(Icons.Filled.Notifications) },
        title = stringResource(R.string.onboarding_notif_title),
        body = stringResource(R.string.onboarding_notif_body),
    ) {
        DarkXPrimaryButton(stringResource(R.string.onboarding_notif_allow), onAllow)
        DarkXTextButton(stringResource(R.string.onboarding_notif_skip), onSkip)
    }
}

@Composable
internal fun DefaultSmsPage(
    state: OnboardingUiState,
    onSetDefault: () -> Unit,
    onOpenSettings: () -> Unit,
    onContinue: () -> Unit,
) {
    val message = when {
        state.isDefaultSmsApp -> stringResource(R.string.onboarding_default_already)
        !state.isRoleAvailable -> stringResource(R.string.onboarding_default_unavailable)
        state.roleRequestDeclined -> stringResource(R.string.onboarding_default_declined)
        else -> null
    }

    OnboardingPage(
        hero = { HeroBadge(Icons.Filled.Email) },
        title = stringResource(R.string.onboarding_default_title),
        body = stringResource(R.string.onboarding_default_body),
        content = { message?.let { InfoCard(it) } },
    ) {
        when {
            state.isDefaultSmsApp -> {
                DarkXPrimaryButton(stringResource(R.string.onboarding_continue), onContinue)
            }
            !state.isRoleAvailable -> {
                DarkXPrimaryButton(stringResource(R.string.onboarding_default_skip), onContinue)
            }
            state.roleRequestDeclined -> {
                DarkXPrimaryButton(stringResource(R.string.onboarding_default_retry), onSetDefault)
                DarkXTextButton(stringResource(R.string.onboarding_default_settings), onOpenSettings)
                DarkXTextButton(stringResource(R.string.onboarding_default_skip), onContinue)
            }
            else -> {
                DarkXPrimaryButton(stringResource(R.string.onboarding_default_set), onSetDefault)
                DarkXTextButton(stringResource(R.string.onboarding_default_skip), onContinue)
            }
        }
    }
}

@Composable
internal fun DonePage(isDefaultSmsApp: Boolean, onFinish: () -> Unit) {
    OnboardingPage(
        hero = {
            HeroBadge(if (isDefaultSmsApp) Icons.Filled.CheckCircle else Icons.Filled.Info)
        },
        title = stringResource(
            if (isDefaultSmsApp) R.string.onboarding_done_title_success
            else R.string.onboarding_done_title_limited,
        ),
        body = stringResource(
            if (isDefaultSmsApp) R.string.onboarding_done_body_success
            else R.string.onboarding_done_body_limited,
        ),
    ) {
        DarkXPrimaryButton(stringResource(R.string.onboarding_get_started), onFinish)
    }
}
