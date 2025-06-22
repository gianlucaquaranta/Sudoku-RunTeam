package it.uniroma2.RunTeam.Sudoku.ui.game

import android.app.Application
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import it.uniroma2.RunTeam.Sudoku.model.Difficulty
import it.uniroma2.RunTeam.Sudoku.ui.game.viewModel.GameViewModel
import it.uniroma2.RunTeam.Sudoku.ui.game.components.GameTopBar
import it.uniroma2.RunTeam.Sudoku.R
import androidx.navigation.NavHostController
import it.uniroma2.RunTeam.Sudoku.ui.game.components.CelebrationConfetti
import it.uniroma2.RunTeam.Sudoku.navigation.NavRoutes
import it.uniroma2.RunTeam.Sudoku.ui.game.components.ResponsiveGameLayout
import it.uniroma2.RunTeam.Sudoku.ui.game.viewModel.GameViewModelFactory

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
    var showRestartDialog by remember { mutableStateOf(false) }

    LaunchedEffect(gameStartMode) {
        if (gameViewModel.uiState.value.sudokuGrid == null) {
            when (gameStartMode) {
                NavRoutes.GAME -> {
                    gameViewModel.createGrid(context, difficulty)
                }
                NavRoutes.RESUME -> {
                    val gameLoaded = gameViewModel.tryResumeGame()
                    if (gameLoaded != null) {
                        currentDifficulty = gameLoaded
                    } else {
                        currentDifficulty = Difficulty.EASY
                        gameViewModel.createGrid(context, currentDifficulty)
                    }
                }
            }
            gameViewModel.startTimer()
        }
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
                onHint = { if (state.remainingHints > 0) gameViewModel.hint() },
                onUndo = { gameViewModel.undo() },
                onRedo = { gameViewModel.redo() },
                onRestart = { showRestartDialog = true},
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

    if (showRestartDialog) {
        RestartGameDialog(
            onDismiss = { showRestartDialog = false },
            onConfirm = {
                showRestartDialog = false
                gameViewModel.restartGame()
            },
            context=context
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
            onDismissRequest = {  },
            title = { Text(context.getString(R.string.congratulations_popup)+"!!!") },
            text = { Text(context.getString(R.string.you_win_popup)+"!") },
            confirmButton = {
                Box(modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center) {
                    Button(onClick = {
                        gameViewModel.createGrid(context, currentDifficulty)
                    }) {
                        Text(context.getString(R.string.new_game_popup))
                    }
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
            onDismissRequest = {  },
            title = { Text(context.getString(R.string.you_lose_popup)+"!") },
            text = {
                val maxErrors = state.errors
                Text( context.getString(R.string.errors_1_popup)+" $maxErrors "+ context.getString(R.string.errors_2_popup)+ " $maxErrors")
            },
            confirmButton = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { gameViewModel.restartGame() },
                    ) {
                        Text(context.getString(R.string.restart_popup))
                    }
                    Button(
                        onClick = { gameViewModel.createGrid(context, currentDifficulty) }
                    ) {
                        Text(context.getString(R.string.new_game_popup))
                    }
                }
            }
        )
    }

}

@Composable
fun RestartGameDialog(onDismiss: () -> Unit, onConfirm: () -> Unit,context: Context) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                // Row con croce a destra
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { onDismiss() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }

                // Messaggio
                Text(
                    text =context.getString(R.string.restart_confirmation),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Bottone di conferma
                Button(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(context.getString(R.string.restart_popup))
                }
            }
        }
    }
}
