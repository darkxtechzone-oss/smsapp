package com.darkx.message.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.darkx.message.R

@Composable
fun StepIndicator(current: Int, total: Int, modifier: Modifier = Modifier) {
    val description = stringResource(R.string.step_indicator_description, current + 1, total)
    Row(
        modifier = modifier.clearAndSetSemantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(total) { index ->
            val selected = index == current
            val width by animateDpAsState(
                targetValue = if (selected) 24.dp else 8.dp,
                animationSpec = tween(220),
                label = "stepWidth",
            )
            val color by animateColorAsState(
                targetValue = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                animationSpec = tween(220),
                label = "stepColor",
            )
            Box(
                Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color),
            )
        }
    }
}
