package com.shantjap.app.presentation.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shantjap.app.presentation.navigation.NavRoutes
import com.shantjap.app.presentation.theme.ModeThemes
import com.shantjap.app.presentation.ui.components.CountStat
import com.shantjap.app.presentation.ui.components.GlowTapCircle
import com.shantjap.app.presentation.ui.components.ModeToggle
import com.shantjap.app.presentation.ui.components.QuoteCard
import com.shantjap.app.presentation.ui.components.SankalpReminderCard
import com.shantjap.app.presentation.viewmodel.MainViewModel
import com.shantjap.app.utils.Haptics

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val mode by viewModel.mode.collectAsState()
    val counts by viewModel.counts.collectAsState()
    val quote by viewModel.quote.collectAsState()
    val sankalpReminder by viewModel.sankalpReminder.collectAsState()
    val context = LocalContext.current
    val modeColors = ModeThemes.forMode(mode)

    LaunchedEffect(Unit) {
        viewModel.refreshAll()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(modeColors.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ModeToggle(mode = mode, onModeChange = viewModel::setMode)
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                GlowTapCircle(
                    color = modeColors.primary,
                    glowColor = modeColors.glow
                ) {
                    Haptics.vibrate(context, mode)
                    viewModel.onTap()
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CountStat(label = "Today", value = counts.today.toString())
                CountStat(label = "Lifetime", value = counts.lifetime.toString())
                CountStat(label = "Streak", value = counts.streak.toString())
            }
        }

        Column {
            if (sankalpReminder != null) {
                SankalpReminderCard(
                    sankalp = sankalpReminder,
                    onDismiss = viewModel::dismissSankalpReminder
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            QuoteCard(
                quote = quote,
                onShare = {
                    val current = quote ?: return@QuoteCard
                    val shareText = "${current.hindi}\n${current.english}"
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share"))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = { navController.navigate(NavRoutes.silentRoute(mode)) }) {
                    Text(text = "Silent")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(onClick = { navController.navigate(NavRoutes.LEADERBOARD) }) {
                    Text(text = "Leaderboard")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Button(onClick = { navController.navigate(NavRoutes.JOURNEY) }) {
                    Text(text = "108 Day Journey")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(onClick = { navController.navigate(NavRoutes.SANKALP) }) {
                    Text(text = "Sankalp")
                }
            }
        }
    }
}
