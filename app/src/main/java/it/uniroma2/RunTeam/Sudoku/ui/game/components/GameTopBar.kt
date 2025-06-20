package it.uniroma2.RunTeam.Sudoku.ui.game.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(    seconds: Int,
                   onHint: () -> Unit,
                   onUndo: () -> Unit,
                   onRedo: () -> Unit,
                   onRestart: () -> Unit,
                   onSaveExit: ()->Unit) {

    TopAppBar(
        title = {
            GameTimer(seconds = seconds)
        },
        actions = {
            IconButton(onClick = onHint) {
                Icon(Icons.Filled.TipsAndUpdates, contentDescription = "Hint")
            }
            IconButton(onClick = onUndo) {
                Icon(Icons.Filled.Undo, contentDescription = "Undo")
            }
            IconButton(onClick = onRedo) {
                Icon(Icons.Filled.Redo, contentDescription = "Redo")
            }
            IconButton(onClick = onRestart) {
                Icon(Icons.Filled.Refresh, contentDescription = "Restart")
            }
            IconButton(onClick = onSaveExit) {
                Icon(Icons.Filled.Home, contentDescription = "Exit")
            }
        }
    )
}
