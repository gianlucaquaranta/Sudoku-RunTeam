package it.uniroma2.RunTeam.Sudoku.ui.game

import android.app.Application
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import it.uniroma2.RunTeam.Sudoku.model.Difficulty
import it.uniroma2.RunTeam.Sudoku.ui.game.viewModel.GameViewModel
import it.uniroma2.RunTeam.Sudoku.ui.game.components.GameTopBar
import it.uniroma2.RunTeam.Sudoku.R
import androidx.navigation.NavHostController
import it.uniroma2.RunTeam.Sudoku.ui.game.components.CelebrationConfetti
import it.uniroma2.RunTeam.Sudoku.navigation.NavRoutes
import it.uniroma2.RunTeam.Sudoku.ui.game.viewModel.GameViewModelFactory
import it.uniroma2.RunTeam.Sudoku.ui.game.components.ResponsiveGameLayout

@Composable
fun GameScreen(navController: NavHostController, gameStartMode: String, difficulty: Difficulty) {

    val context = LocalContext.current
    val application = context.applicationContext as Application
    val gameViewModel: GameViewModel = viewModel(
        factory = GameViewModelFactory(application)
    )
    val state by gameViewModel.uiState.collectAsState()
    val navigateHome by gameViewModel.shouldNavigateHome.collectAsState()
    val gameState by gameViewModel.uiState.collectAsState()
    val configuration = androidx.compose.ui.platform.LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    var currentDifficulty = difficulty

    LaunchedEffect(gameStartMode) { // Esegui questo effetto quando gameStartMode cambia (o all'inizio)
        when (gameStartMode) {
            NavRoutes.GAME -> {
                gameViewModel.createGrid(context, difficulty)
            }
            NavRoutes.RESUME -> {
                val gameLoaded = gameViewModel.tryResumeGame()
                if (gameLoaded!=null) {
                    currentDifficulty = gameLoaded //Setto qui la difficoltà della partita caricata
                }else{
                    currentDifficulty = Difficulty.EASY //Eventualità nel caso in cui non trovi nessuna partita caricata
                    gameViewModel.createGrid(context, currentDifficulty)
                }
            }
        }
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
        LaunchedEffect(gameState.isGameCompleted) {
            gameViewModel.stopTimer()
            gameViewModel.updateGameStatsInternal( //Aggiorna le statistiche dopo una partita vinta
                difficulty = currentDifficulty,// Passa la difficoltà corrente
                true //isWin true
            )
        }
        AlertDialog(
            onDismissRequest = { /* Puoi ignorare o fare qualcosa */ },
            title = { Text(context.getString(R.string.congratulations_popup)+"!!!") },
            text = { Text(context.getString(R.string.you_win_popup)+"!") },
            confirmButton = {
                Button(onClick = {
                    gameViewModel.createGrid(context, currentDifficulty)// per ora solo easy poi ovviamente con una variabile scelta all'inizio si settera qua
                }) {
                    Text(context.getString(R.string.new_game_popup))
                }
            }
        )
        CelebrationConfetti(show = true)
    }

    if (gameState.isGameLost) {
        LaunchedEffect(gameState.isGameCompleted) {
            gameViewModel.stopTimer()
            gameViewModel.updateGameStatsInternal( //Aggiorna le statistiche dopo una partita vinta
                difficulty = currentDifficulty,// Passa la difficoltà corrente
                false //isWin false
            )
        }
        AlertDialog(
            onDismissRequest = { /* Puoi ignorare o fare qualcosa */ },
            title = { Text(context.getString(R.string.new_game_popup)+"!") },
            text = {
                val maxErrors = state.errors
                Text( context.getString(R.string.errors_1_popup)+"$maxErrors"+ context.getString(R.string.errors_2_popup)+ "$maxErrors")
            },
            confirmButton = {
                Button(onClick = {
                    gameViewModel.restartGame()
                }) {
                    Text(context.getString(R.string.restart_popup))
                }
                Button(onClick = {
                    gameViewModel.createGrid(context, currentDifficulty)
                }) {
                    Text(context.getString(R.string.new_game_popup))
                }
            }
        )
    }
}
