package it.uniroma2.RunTeam.Sudoku.ui.game.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun GameTimer(seconds:Int) {
    val minutesPart = seconds / 60
    val secondsPart = seconds % 60
    val timeFormatted = String.format("%02d:%02d", minutesPart, secondsPart)

    Text(
        text = "$timeFormatted",
        style = MaterialTheme.typography.titleLarge
    )
}

