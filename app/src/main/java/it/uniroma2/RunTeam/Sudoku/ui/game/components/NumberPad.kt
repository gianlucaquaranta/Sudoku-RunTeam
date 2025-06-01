package it.uniroma2.RunTeam.Sudoku.ui.game.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun NumberPad(isNoteMode: Boolean, onNoteToggle: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.wrapContentWidth()
    )
    {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.width(200.dp),  // Adatta alla larghezza contenuto
            horizontalArrangement = Arrangement.Center // Centra orizzontalmente
        ) {
            items(9) { num ->
                  NumberButton(
                    number = num + 1,
                    onClick = { /* TODO */ }
                )
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


@Composable
fun NumberButton(number: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(6.dp)
            .size(72.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            contentAlignment = Center,
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }
        ) {
            Text(
                text = "$number",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NumberPadPreview() {
    var isNoteMode by remember { mutableStateOf(false) }

    NumberPad(
        isNoteMode = isNoteMode,
        onNoteToggle = { isNoteMode = !isNoteMode }
    )
}


