package it.uniroma2.RunTeam.Sudoku.ui.game

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.uniroma2.RunTeam.Sudoku.ui.game.ViewModel.GameTimerViewModel
import it.uniroma2.RunTeam.Sudoku.ui.game.components.GameTimer
import it.uniroma2.RunTeam.Sudoku.ui.game.components.GameTopBar
import it.uniroma2.RunTeam.Sudoku.ui.game.components.NumberPad
import it.uniroma2.RunTeam.Sudoku.ui.game.components.SuggestionButton
import it.uniroma2.RunTeam.Sudoku.ui.game.components.SudokuGrid
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun GameScreen() {
    val viewModel: GameTimerViewModel = viewModel()
    val seconds = viewModel.seconds
    var isNoteMode by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.startTimer()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameTopBar(seconds)
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            SudokuGrid()
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            NumberPad(
                isNoteMode = isNoteMode,
                onNoteToggle = { isNoteMode = !isNoteMode }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        SuggestionButton()
    }
}