package it.uniroma2.RunTeam.Sudoku.ui.game.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun NumberPad(
    isNoteMode: Boolean,
    onNoteToggle: () -> Unit,
    onNumberClick: (Int) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        // Icona gomma (cancella) a sinistra
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier
                .weight(1f)
                .size(50.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Backspace,
                contentDescription = "Cancella",
                modifier = Modifier.size(35.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        // Tastierino numerico centrale
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .weight(2.7f)
                .width(240.dp),
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

        // Icona matita (modalità note) a destra
        IconButton(
            onClick = onNoteToggle,
            modifier = Modifier
                .weight(1f)
                .size(50.dp)
        ) {
            Icon(
                imageVector = if (isNoteMode) Icons.Filled.Edit else Icons.Outlined.Edit,
                contentDescription = "Modalità nota",
                modifier = Modifier.size(40.dp),
                tint = if (isNoteMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun NumberButton(number: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() }
            .padding(start = 2.dp, end = 2.dp, bottom = 4.dp),
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
