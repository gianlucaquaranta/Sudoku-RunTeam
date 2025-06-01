package it.uniroma2.RunTeam.Sudoku.ui.game.components

// con NumberPad, NumberKey, NumberButton
// ...
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun NumberPad(
    isNoteMode: Boolean,
    onNoteToggle: () -> Unit,
    onNumberClick: (Int) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.wrapContentWidth()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.width(240.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(9) { num ->
                NumberButton(
                    number = num + 1,
                    onClick = { onNumberClick(num + 1) }
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            NumberKey(label = "Del", onClick = onDeleteClick, modifier = Modifier.weight(1f))
            NumberKey(label = "Note", onClick = onNoteToggle, modifier = Modifier.weight(1f)) // isSelected rimosso per ora, gestito da GameScreen
        }
    }
}

@Composable
fun NumberKey(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false // Aggiungiamo isSelected per il tasto Note
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp),
        colors = if (isSelected) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        else ButtonDefaults.buttonColors()
    ) {
        Text(label)
    }
}


@Composable
fun NumberButton(number: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "$number",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun NumberPadPreview() {
    var isNoteMode by remember { mutableStateOf(false) }
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            NumberPad(
                isNoteMode = isNoteMode,
                onNoteToggle = { isNoteMode = !isNoteMode },
                onNumberClick = { println("Numero: $it") },
                onDeleteClick = { println("Delete") }
            )
        }
    }
}