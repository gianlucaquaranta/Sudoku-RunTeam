package it.uniroma2.RunTeam.Sudoku.ui.game.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NumberPad(isNoteMode: Boolean, onNoteToggle: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.wrapContentWidth()
    ) {
        for (row in 0 until 3) {
            Row(horizontalArrangement = Arrangement.Center) {
                for (col in 1..3) {
                    val number = row * 3 + col
                    NumberKey(label = number.toString(), onClick = { /* TODO */ })
                }
            }
        }
        Row(horizontalArrangement = Arrangement.Center) {
            NumberKey(label = "Del", onClick = { /* TODO */ })
            NumberKey(label = "Note", onClick = onNoteToggle, isSelected = isNoteMode)
        }
    }
}

@Composable
fun NumberKey(label: String, onClick: () -> Unit, isSelected: Boolean = false) {
    val backgroundColor = if (isSelected) Color.LightGray else MaterialTheme.colorScheme.primary
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(4.dp)
            .size(64.dp)
    ) {
        Text(label)
    }
}