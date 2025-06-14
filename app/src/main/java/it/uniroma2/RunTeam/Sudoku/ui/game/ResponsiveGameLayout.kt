package it.uniroma2.RunTeam.Sudoku.ui.game

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import it.uniroma2.RunTeam.Sudoku.ui.game.ViewModel.GameState
import it.uniroma2.RunTeam.Sudoku.ui.game.ViewModel.GameViewModel
import it.uniroma2.RunTeam.Sudoku.ui.game.components.SudokuGrid
import it.uniroma2.RunTeam.Sudoku.ui.game.components.SuggestionButton
import it.uniroma2.RunTeam.Sudoku.ui.game.components.NumberPad


@Composable
fun ResponsiveGameLayout(
    state: GameState,
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues()
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Griglia
            Box(
                modifier = Modifier
                    .weight(1f)
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

            // Tastierino + Suggerimenti
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                NumberPad(
                    isNoteMode = state.isNoteMode,
                    onNoteToggle = { gameViewModel.toggleNoteMode() },
                    onNumberClick = { number ->
                        state.selectedCell?.let { cell ->
                            if (state.isNoteMode) cell.toggleNote(number)
                            else if (cell.value == number && !cell.isValid) gameViewModel.clearCellValue(cell)
                            else gameViewModel.updateCellValue(cell, number)
                        }
                    },
                    onDeleteClick = {
                        state.selectedCell?.let { cell ->
                            if (cell.notes.isNotEmpty()) cell.clearNotes()
                            else gameViewModel.clearCellValue(cell)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                SuggestionButton()
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                state.sudokuGrid?.let {
                    SudokuGrid(
                        gridCells = it.grid,
                        currentlySelectedCell = state.selectedCell,
                        onCellClick = { gameViewModel.onCellSelected(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .aspectRatio(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            NumberPad(
                isNoteMode = state.isNoteMode,
                onNoteToggle = { gameViewModel.toggleNoteMode() },
                onNumberClick = { number ->
                    state.selectedCell?.let { cell ->
                        if (state.isNoteMode) cell.toggleNote(number)
                        else if (cell.value == number && !cell.isValid) gameViewModel.clearCellValue(cell)
                        else gameViewModel.updateCellValue(cell, number)
                    }
                },
                onDeleteClick = {
                    state.selectedCell?.let { cell ->
                        if (cell.notes.isNotEmpty()) cell.clearNotes()
                        else gameViewModel.clearCellValue(cell)
                    }
                },
                modifier = Modifier.fillMaxWidth(0.85f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            SuggestionButton()
        }
    }
}
