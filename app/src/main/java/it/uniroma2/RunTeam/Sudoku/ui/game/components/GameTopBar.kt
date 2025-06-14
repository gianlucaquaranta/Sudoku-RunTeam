package it.uniroma2.RunTeam.Sudoku.ui.game.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.uniroma2.RunTeam.Sudoku.ui.game.ViewModel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(    seconds: Int,
                   onUndo: () -> Unit,
                   onRedo: () -> Unit,
                   onRestart: () -> Unit,
                   onSaveExit: ()->Unit) {

    TopAppBar(
        title = { },
        actions = {
            IconButton(onClick = onUndo) {
                Icon(Icons.Default.Undo, contentDescription = "Undo")
            }
            IconButton(onClick = onRedo) {
                Icon(Icons.Default.Redo, contentDescription = "Redo")
            }
            IconButton(onClick = onRestart) {
                Icon(Icons.Default.Refresh, contentDescription = "Restart")
            }
            IconButton(onClick = { /* theme */ }) {
                Icon(Icons.Default.DarkMode, contentDescription = "Change Theme")
            }
            IconButton(onClick = onSaveExit) {
                Icon(Icons.Default.Home, contentDescription = "Exit")
            }
        },
        navigationIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "Timer"
                )
                Spacer(modifier = Modifier.width(4.dp))
                GameTimer(seconds = seconds)
            }
        }
    )
}
