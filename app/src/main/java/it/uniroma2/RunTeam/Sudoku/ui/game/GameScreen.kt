package it.uniroma2.RunTeam.Sudoku.ui.game

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.uniroma2.RunTeam.Sudoku.model.Difficulty
import it.uniroma2.RunTeam.Sudoku.ui.game.viewModel.GameViewModel
import it.uniroma2.RunTeam.Sudoku.ui.game.components.GameTopBar
import it.uniroma2.RunTeam.Sudoku.ui.game.components.NumberPad
import it.uniroma2.RunTeam.Sudoku.ui.game.components.SudokuGrid
import it.uniroma2.RunTeam.Sudoku.ui.game.components.SuggestionButton
import androidx.navigation.NavHostController
import it.uniroma2.RunTeam.Sudoku.CelebrationConfetti
import it.uniroma2.RunTeam.Sudoku.ui.game.viewModel.GameViewModelFactory

@Composable
fun GameScreen(navController: NavHostController) {

    val context = LocalContext.current
    val application = context.applicationContext as Application
    val gameViewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(application)
    )

    val state by gameViewModel.uiState.collectAsState()
    val navigateHome by gameViewModel.shouldNavigateHome.collectAsState()
    val gameState by gameViewModel.uiState.collectAsState()


    LaunchedEffect(Unit) {
        val difficulty: Difficulty = Difficulty.EASY
        gameViewModel.createGrid(context, difficulty)
        gameViewModel.startTimer()
    }

    LaunchedEffect(navigateHome) {
        if (navigateHome) {
            navController.popBackStack()
            gameViewModel.resetNavigateHomeFlag()//questo popBackStack serve per non creare un'ulteriore pagina home
        }
    }
    Scaffold(
        topBar = {
            GameTopBar(
                seconds = state.secondsElapsed,
                onUndo = { gameViewModel.undo() },
                onRedo = { gameViewModel.redo() },
                onRestart = { gameViewModel.restartGame()},
                onSaveExit={gameViewModel.onSaveExit()},
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (state.isLoading) {
                // Mostra un indicatore di caricamento o un testo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    // Puoi usare CircularProgressIndicator o un Text
                    CircularProgressIndicator()
                    // Oppure:
                    // Text("Caricamento griglia...")
                }
            } else {
                // Mostra la SudokuGrid quando non sta caricando
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
                        if (state.isNoteMode) {
                            cell.toggleNote(number)
                        } else {
                            if (cell.value == number && !cell.isValid) {
                                gameViewModel.clearCellValue(cell)
                            } else {
                                gameViewModel.updateCellValue(cell, number)
                            }
                        }
                    }
                },
                onDeleteClick = {
                    state.selectedCell?.let { cell ->
                        if (state.isNoteMode && cell.notes.isNotEmpty()) {
                            cell.clearNotes()
                        } else {
                            gameViewModel.clearCellValue(cell)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.85f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            SuggestionButton()
        }
    }
    if (gameState.isGameCompleted) {
        AlertDialog(
            onDismissRequest = { /* Puoi ignorare o fare qualcosa */ },
            title = { Text("Complimenti!") },
            text = { Text("Hai completato il Sudoku!") },
            confirmButton = {
                Button(onClick = {
                    gameViewModel.createGrid(context,Difficulty.EASY)// per ora solo easy poi ovviamente con una variabile scelta all'inizio si settera qua
                }) {
                    Text("Nuova Partita")
                }
            }
        )
        CelebrationConfetti(show = true)
    }
}
