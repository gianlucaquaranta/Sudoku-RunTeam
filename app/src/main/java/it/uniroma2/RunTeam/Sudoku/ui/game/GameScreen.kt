package it.uniroma2.RunTeam.Sudoku.ui.game

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
import it.uniroma2.RunTeam.Sudoku.ui.game.ViewModel.GameViewModel
import it.uniroma2.RunTeam.Sudoku.ui.game.components.GameTopBar
import it.uniroma2.RunTeam.Sudoku.ui.game.components.NumberPad
import it.uniroma2.RunTeam.Sudoku.ui.game.components.SudokuGrid
import it.uniroma2.RunTeam.Sudoku.ui.game.components.SuggestionButton
import it.uniroma2.RunTeam.Sudoku.R
import androidx.navigation.NavHostController
import it.uniroma2.RunTeam.Sudoku.CelebrationConfetti

@Composable
fun GameScreen(navController: NavHostController,gameViewModel: GameViewModel = viewModel()) {

    val state by gameViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val navigateHome by gameViewModel.shouldNavigateHome.collectAsState()
    val gameState by gameViewModel.uiState.collectAsState()
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

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
        ResponsiveGameLayout(
            state = state,
            gameViewModel = gameViewModel,
            paddingValues = paddingValues
        )
        }

    if (gameState.isGameCompleted) {
        AlertDialog(
            onDismissRequest = { /* Puoi ignorare o fare qualcosa */ },
            title = { Text(context.getString(R.string.congratulations_popup)+"!!!") },
            text = { Text(context.getString(R.string.you_win_popup)+"!") },
            confirmButton = {
                Button(onClick = {
                    gameViewModel.createGrid(context,Difficulty.EASY)// per ora solo easy poi ovviamente con una variabile scelta all'inizio si settera qua
                }) {
                    Text(context.getString(R.string.new_game_popup))
                }
            }
        )
        CelebrationConfetti(show = true)
    }

    if (gameState.isGameLost) {
        AlertDialog(
            onDismissRequest = { /* Puoi ignorare o fare qualcosa */ },
            title = { Text(context.getString(R.string.new_game_popup)+"!") },
            text = {
                val maxErrors = state.errors;
                Text( context.getString(R.string.errors_1_popup)+"$maxErrors"+ context.getString(R.string.errors_2_popup)+ "$maxErrors")
            },
            confirmButton = {
                Button(onClick = {
                    gameViewModel.restartGame()// per ora solo easy poi ovviamente con una variabile scelta all'inizio si settera qua
                }) {
                    Text(context.getString(R.string.restart_popup))
                }
                Button(onClick = {
                    gameViewModel.createGrid(context,Difficulty.EASY)
                }) {
                    Text(context.getString(R.string.new_game_popup))
                }
            }
        )
    }
}
