package com.shantjap.app.presentation.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.shantjap.app.domain.model.Mode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeToggle(
    mode: Mode,
    onModeChange: (Mode) -> Unit
) {
    SingleChoiceSegmentedButtonRow {
        SegmentedButton(
            selected = mode == Mode.RADHA,
            onClick = { onModeChange(Mode.RADHA) },
            label = { Text(text = "Radha Naam") }
        )
        SegmentedButton(
            selected = mode == Mode.RAM,
            onClick = { onModeChange(Mode.RAM) },
            label = { Text(text = "Ram Naam") }
        )
    }
}
