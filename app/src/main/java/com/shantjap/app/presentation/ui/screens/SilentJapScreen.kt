package com.shantjap.app.presentation.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shantjap.app.domain.model.Mode
import com.shantjap.app.presentation.theme.ModeThemes
import com.shantjap.app.presentation.viewmodel.MainViewModel
import com.shantjap.app.utils.Haptics

@Composable
fun SilentJapScreen(
    navController: NavController,
    mode: Mode,
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val modeColors = ModeThemes.forMode(mode)
    var sessionCount by remember { mutableIntStateOf(0) }
    var showSummary by remember { mutableStateOf(false) }

    LaunchedEffect(mode) {
        viewModel.setMode(mode)
    }

    val pulse = rememberInfiniteTransition(label = "silent-glow")
    val glowAlpha by pulse.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "silent-glow-alpha"
    )

    BackHandler {
        showSummary = true
    }

    if (showSummary) {
        AlertDialog(
            onDismissRequest = { showSummary = false },
            title = { Text(text = "Session Complete") },
            text = { Text(text = "You completed $sessionCount chants.") },
            confirmButton = {
                TextButton(onClick = { navController.popBackStack() }) {
                    Text(text = "Close")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSummary = false }) {
                    Text(text = "Continue")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(modeColors.background)
            .pointerInput(Unit) {
                detectTapGestures {
                    sessionCount += 1
                    Haptics.vibrate(context, mode)
                    viewModel.onTap()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(modeColors.primary, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(modeColors.glow.copy(alpha = glowAlpha), Color.Transparent)
                    ),
                    CircleShape
                )
        )
    }
}
