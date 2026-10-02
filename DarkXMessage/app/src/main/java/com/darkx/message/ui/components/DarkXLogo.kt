package com.darkx.message.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.darkx.message.R

@Composable
fun DarkXLogo(modifier: Modifier = Modifier, size: Dp = 96.dp) {
    val start = MaterialTheme.colorScheme.primary
    val end = MaterialTheme.colorScheme.tertiary
    val dotColor = MaterialTheme.colorScheme.primaryContainer
    val description = stringResource(R.string.app_name)

    Canvas(
        modifier = modifier
            .size(size)
            .semantics { contentDescription = description },
    ) {
        val s = this.size.minDimension
        drawRoundRect(
            brush = Brush.linearGradient(listOf(start, end)),
            size = Size(s, s),
            cornerRadius = CornerRadius(s * 0.28f),
        )
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(s * 0.22f, s * 0.26f),
            size = Size(s * 0.56f, s * 0.38f),
            cornerRadius = CornerRadius(s * 0.12f),
        )
        val tail = Path().apply {
            moveTo(s * 0.34f, s * 0.60f)
            lineTo(s * 0.32f, s * 0.78f)
            lineTo(s * 0.50f, s * 0.60f)
            close()
        }
        drawPath(tail, color = Color.White)
        listOf(0.36f, 0.50f, 0.64f).forEach { x ->
            drawCircle(color = dotColor, radius = s * 0.035f, center = Offset(s * x, s * 0.45f))
        }
    }
}
