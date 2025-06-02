package it.uniroma2.RunTeam.Sudoku.ui.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.uniroma2.RunTeam.Sudoku.model.Cell
import it.uniroma2.RunTeam.Sudoku.ui.game.ViewModel.GameTimerViewModel
import it.uniroma2.RunTeam.Sudoku.ui.game.components.GameTopBar
import it.uniroma2.RunTeam.Sudoku.ui.game.components.NumberPad
import it.uniroma2.RunTeam.Sudoku.ui.game.components.SudokuGrid
import it.uniroma2.RunTeam.Sudoku.ui.game.components.SuggestionButton

// Funzione helper per creare la griglia iniziale usando la tua classe Cell
// I puzzle iniziali dovrebbero usare null o 0 per le celle vuote.
// La tua Cell.value di default è 0, quindi questo va bene.
fun createInitialCellGrid(initialPuzzle: List<List<Int?>>): List<List<Cell>> {
    return initialPuzzle.mapIndexed { rowIndex, rowData ->
        rowData.mapIndexed { colIndex, cellValue ->
            val isStarting = cellValue != null && cellValue != 0
            Cell(
                row = rowIndex,
                col = colIndex,
                isStartingCell = isStarting,
                initialValue = if (isStarting) cellValue else null,
                oldValue = null,
                newValue = null// Passa il valore iniziale
            )
            // Non c'è più bisogno dell'apply block per impostare il valore iniziale
            // se il costruttore di Cell e la proprietà 'value' sono configurati correttamente.
        }
    }
}

// Esempio di puzzle (null per vuoto, così Cell.isStartingCell funziona correttamente)
val examplePuzzle: List<List<Int?>> = listOf(
    listOf(null, null, null, null, null, null, null, null, null),
    listOf(null, null, null, null, null, null, null, null, null),
    listOf(null, null, null, null, null, null, null, null, null),
    listOf(null, null, null, null, null, null, null, null, null),
    listOf(null, null, null, null, null, null, null, null, null),
    listOf(null, null, null, null, null, null, null, null, null),
    listOf(null, null, null, null, null, null, null, null, null),
    listOf(null, null, null, null, null, null, null, null, null),
    listOf(null, null, null, null, null, null, null, null, null)
)

@Composable
fun GameScreen(gameTimerViewModel: GameTimerViewModel = viewModel()) {
    val viewModel: GameTimerViewModel = viewModel()
    val seconds = viewModel.seconds
    var isNoteMode by remember { mutableStateOf(false) }

    val sudokuGrid: List<List<Cell>> = remember { createInitialCellGrid(examplePuzzle) }
    var selectedCell: Cell? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        viewModel.startTimer()
    }

    // Quando selectedCell cambia, aggiorna il flag isSelected nelle celle
    // Questo è un modo per usare il tuo Cell.isSelected se lo desideri,
    // altrimenti SudokuGrid usa solo `currentlySelectedCell` per l'evidenziazione.
    LaunchedEffect(selectedCell) {
        sudokuGrid.flatten().forEach { cell ->
            cell.isSelected = (cell == selectedCell && !cell.isStartingCell)
        }
    }

    LaunchedEffect(Unit) {
        gameTimerViewModel.startTimer()
        gameTimerViewModel.sudokuGrid = sudokuGrid
    }

    Scaffold(
        topBar = { GameTopBar(
            seconds = seconds,
            onUndo = { gameTimerViewModel.undo() },
            onRedo = { gameTimerViewModel.redo() }
        ) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SudokuGrid(
                gridCells = sudokuGrid,
                currentlySelectedCell = selectedCell,
                onCellClick = { cell ->
                    if (!cell.isStartingCell) {
                        selectedCell = if (selectedCell == cell) null else cell // Tocca di nuovo per deselezionare
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .aspectRatio(1f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            NumberPad(
                isNoteMode = isNoteMode,
                onNoteToggle = { isNoteMode = !isNoteMode },
                onNumberClick = { number ->
                    selectedCell?.let { cell ->
                        if (!cell.isStartingCell) {
                            if (isNoteMode) {
                                cell.toggleNote(number)
                            } else {
                                if (cell.value == number) {
                                    gameTimerViewModel.clearCellValue(cell)
                                } else {
                                    gameTimerViewModel.updateCellValue(cell, number)
                                }
                            }
                        }
                    }
                },
                onDeleteClick = {
                    selectedCell?.let { cell ->
                        if (!cell.isStartingCell) {
                            if (isNoteMode && cell.notes.isNotEmpty()) {
                                cell.clearNotes()
                            } else {
                                gameTimerViewModel.clearCellValue(cell)
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.85f) // Leggermente più largo
            )

            Spacer(modifier = Modifier.height(16.dp))

            SuggestionButton(
            )
        }
    }
}


@Preview(showBackground = true, device = "spec:width=411dp,height=891dp,dpi=420")
@Composable
fun GameScreenPreviewWithYourCell() {
    MaterialTheme {
        // Per il Preview, potremmo voler passare un ViewModel fittizio se necessario
        GameScreen()
    }
}