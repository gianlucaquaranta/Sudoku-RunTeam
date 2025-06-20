package it.uniroma2.RunTeam.Sudoku.ui.game.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.uniroma2.RunTeam.Sudoku.model.Cell

@Composable
fun SudokuGrid(
    gridCells: Array<Array<Cell>>,
    currentlySelectedCell: Cell?, // La cella attualmente selezionata, gestita da GameScreen
    onCellClick: (cell: Cell) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .aspectRatio(1f)
                .border(BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)) // Bordo più sottile
                .padding(1.dp)
        ) {
            gridCells.forEachIndexed { rowIndex, row ->
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    row.forEachIndexed { colIndex, cell ->
                        val isThisCellSelected = currentlySelectedCell == cell
                        // Determina il colore di sfondo
                        val backgroundColor = when {
                            isThisCellSelected && !cell.isIncorrect-> MaterialTheme.colorScheme.inversePrimary //Evidenzia la selezione
                            cell.isIncorrect -> MaterialTheme.colorScheme.error // Evidenzia errore
                            // Alternating background per i blocchi 3x3, più sottile
                            (rowIndex / 3 + colIndex / 3) % 2 == 0 -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            else -> MaterialTheme.colorScheme.surface
                        }
                        SudokuCellDisplay( // Rinominato per chiarezza
                            cell = cell,
                            isSelected = isThisCellSelected, // Passa lo stato di selezione calcolato
                            backgroundColor = backgroundColor,
                            onClick = { onCellClick(cell) },
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        )
                        if (colIndex < 8) { // Bordo verticale tra le celle
                            val color = if ((colIndex + 1) % 3 == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            Spacer(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(if ((colIndex + 1) % 3 == 0) 1.5.dp else 0.5.dp)
                                    .background(color)
                            )
                        }
                    }
                }
                if (rowIndex < gridCells.size - 1) { // Bordo orizzontale tra le righe
                    val color = if ((rowIndex + 1) % 3 == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if ((rowIndex + 1) % 3 == 0) 1.5.dp else 0.5.dp)
                            .background(color)
                    )
                }
            }
        }
    }
}

@Composable
fun SudokuCellDisplay(
    cell: Cell,
    isSelected: Boolean, // Riceve lo stato di selezione
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(backgroundColor)
            .clickable(enabled = !(cell.isStartingCell||cell.isValid)) { onClick() }
            .padding(1.dp)
    ) {
        // Se ci sono note e nessun valore principale, mostra le note
        if (cell.value == 0 && cell.notes.isNotEmpty()) {
            // Layout per le note (massimo 9 note in una griglia 3x3)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp),
                userScrollEnabled = false // Non serve scrollare qui
            ) {
                items(9) { index ->
                    val noteNumber = index + 1
                    if (cell.notes.contains(noteNumber)) {
                        Text(
                            text = noteNumber.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.5.sp, lineHeight = 8.5.sp), // Molto piccolo
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .aspectRatio(1f) // quadrato
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    } else {
                        Spacer(Modifier.fillMaxSize()) // Spazio vuoto se la nota non c'è
                    }
                }
            }
        } else if (cell.value != 0) {
            // Mostra il valore principale della cella
            Text(
                text = cell.displayValue, // Usa la proprietà displayValue
                style = TextStyle(
                    fontSize = 24.sp, // Leggermente più grande
                    fontWeight = if (cell.isStartingCell) FontWeight.Bold else FontWeight.Medium,
                ),
                color = when {
                    cell.isStartingCell -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.87f)
                    cell.isIncorrect -> MaterialTheme.colorScheme.onErrorContainer
                    else -> MaterialTheme.colorScheme.primary
                }
            )
        }
        // Se la cella è vuota e non ci sono note, rimane vuota (Box vuoto)
    }
}
