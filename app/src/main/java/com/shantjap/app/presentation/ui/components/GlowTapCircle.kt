package com.shantjap.app.presentation.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun GlowTapCircle(
    color: Color,
    glowColor: Color,
    onTap: () -> Unit
) {
    var pulse by remember { mutableIntStateOf(0) }
    val glow = remember { Animatable(0f) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(pulse) {
        glow.snapTo(0.9f)
        glow.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .size(240.dp)
            .clip(CircleShape)
            .background(color)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                pulse += 1
                onTap()
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer { alpha = glow.value }
                .background(
                    Brush.radialGradient(
                        colors = listOf(glowColor, Color.Transparent)
                    ),
                    CircleShape
                )
        )
        Text(
            text = "Tap",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
    }
}
