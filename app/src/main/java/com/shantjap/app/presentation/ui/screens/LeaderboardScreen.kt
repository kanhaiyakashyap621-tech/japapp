package com.shantjap.app.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shantjap.app.domain.model.LeaderboardEntry
import com.shantjap.app.presentation.ui.components.ModeToggle
import com.shantjap.app.presentation.viewmodel.LeaderboardViewModel

@Composable
fun LeaderboardScreen(
    navController: NavController,
    viewModel: LeaderboardViewModel = viewModel()
) {
    val mode by viewModel.mode.collectAsState()
    val name by viewModel.name.collectAsState()
    val city by viewModel.city.collectAsState()
    val country by viewModel.country.collectAsState()
    val daily by viewModel.daily.collectAsState()
    val weekly by viewModel.weekly.collectAsState()
    val lifetime by viewModel.lifetime.collectAsState()
    val globalTotal by viewModel.globalTotal.collectAsState()
    val syncing by viewModel.syncing.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshRankings()
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    val items = when (selectedTab) {
        0 -> daily
        1 -> weekly
        else -> lifetime
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        ModeToggle(mode = mode, onModeChange = viewModel::setMode)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Global Total: $globalTotal", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = viewModel::updateName,
                    label = { Text(text = "Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = city,
                    onValueChange = viewModel::updateCity,
                    label = { Text(text = "City") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Country: $country", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = viewModel::saveProfile) {
                        Text(text = "Save Profile")
                    }
                    Button(onClick = viewModel::syncCounts, enabled = !syncing) {
                        Text(text = if (syncing) "Syncing..." else "Sync Counts")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Daily") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Weekly") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Lifetime") })
        }
        Spacer(modifier = Modifier.height(8.dp))
        LeaderboardList(
            items = items,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun LeaderboardList(items: List<LeaderboardEntry>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(items) { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${index + 1}. ${item.name} (${item.city})")
                Text(text = item.count.toString())
            }
        }
    }
}
