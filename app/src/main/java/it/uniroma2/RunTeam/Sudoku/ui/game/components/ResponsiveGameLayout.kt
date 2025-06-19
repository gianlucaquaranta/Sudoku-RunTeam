package it.uniroma2.RunTeam.Sudoku.ui.game.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import it.uniroma2.RunTeam.Sudoku.ui.game.ViewModel.GameState
import it.uniroma2.RunTeam.Sudoku.ui.game.viewModel.GameViewModel


@Composable
fun ResponsiveGameLayout(
    state: GameState,
    gameViewModel: GameViewModel,
    paddingValues: PaddingValues
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(8.dp)
    ) {
        if (isLandscape) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Parte sinistra: Sudoku Grid
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .size((configuration.screenWidthDp)*0.8)
                        .aspectRatio(1f)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        state.sudokuGrid?.let {
                            SudokuGrid(
                                gridCells = it.grid,
                                currentlySelectedCell = state.selectedCell,
                                onCellClick = { gameViewModel.onCellSelected(it) },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                // Parte destra: NumberPad
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 8.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    NumberPad(
                        isNoteMode = state.isNoteMode,
                        onNoteToggle = { gameViewModel.toggleNoteMode() },
                        onNumberClick = { number ->
                            handleNumberClick(
                                state,
                                number,
                                gameViewModel
                            )
                        },
                        onDeleteClick = { handleDeleteClick(state, gameViewModel) },
                        modifier = Modifier.fillMaxWidth(0.85f)
                    )
                }
            }

        } else {
            // PORTRAIT: Griglia sopra, tastiera sotto
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        state.sudokuGrid?.let {
                            SudokuGrid(
                                gridCells = it.grid,
                                currentlySelectedCell = state.selectedCell,
                                onCellClick = { gameViewModel.onCellSelected(it) },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Errori: ${state.errors}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "Suggerimenti: ${state.remainingHints}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                NumberPad(
                    isNoteMode = state.isNoteMode,
                    onNoteToggle = { gameViewModel.toggleNoteMode() },
                    onNumberClick = { number -> handleNumberClick(state, number, gameViewModel) },
                    onDeleteClick = { handleDeleteClick(state, gameViewModel) },
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

            }
        }
    }
}

private fun handleNumberClick(
    state: GameState,
    number: Int,
    viewModel: GameViewModel
) {
    state.selectedCell?.let { cell ->
        if (state.isNoteMode) {
            cell.toggleNote(number)
        } else {
            if (cell.value == number && !cell.isValid) {
                viewModel.clearCellValue(cell)
            } else {
                viewModel.updateCellValue(cell, number)
            }
        }
    }
}

private fun handleDeleteClick(
    state: GameState,
    viewModel: GameViewModel
) {
    state.selectedCell?.let { cell ->
        if (cell.notes.isNotEmpty()) {
            cell.clearNotes()
        } else {
            viewModel.clearCellValue(cell)
        }
    }
}


