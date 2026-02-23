package com.shantjap.app.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shantjap.app.presentation.viewmodel.JourneyViewModel

@Composable
fun JourneyScreen(
    navController: NavController,
    viewModel: JourneyViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(text = "108 Day Journey", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (!state.active) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Commit to 108 days of gentle, steady jap.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = viewModel::joinJourney) {
                        Text(text = "Join Journey")
                    }
                }
            }
            return
        }

        if (state.completed) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Journey Complete",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You have completed 108 days. May your practice deepen.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            return
        }

        val progress = (state.currentDay.coerceAtLeast(0) / 108f).coerceIn(0f, 1f)
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Day ${state.currentDay} of 108", style = MaterialTheme.typography.titleMedium)
        Text(text = "Current streak: ${state.currentDay} days", style = MaterialTheme.typography.bodyMedium)

        val unlockMessage = unlockMessageForDay(state.currentDay)
        if (unlockMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Unlocked", style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = unlockMessage, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

private fun unlockMessageForDay(day: Int): String? {
    return when (day) {
        12 -> "Your breath feels quieter. Keep the rhythm steady."
        24 -> "Your mind rests more often between each name."
        36 -> "The practice is starting to carry you."
        48 -> "You return faster when the mind wanders."
        60 -> "Your jap has a gentle momentum now."
        72 -> "The names feel lighter on the heart."
        84 -> "Your focus softens into clarity."
        96 -> "You are close to completion. Stay kind and steady."
        108 -> "Completion unlocked. Offer gratitude and continue softly."
        else -> null
    }
}
