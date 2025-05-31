package it.uniroma2.RunTeam.Sudoku.ui.game.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar() {
    TopAppBar(
        title = { Text("Sudoku") },
        actions = {
            IconButton(onClick = { /* undo */ }) {
                Icon(Icons.Default.Undo, contentDescription = "Undo")
            }
            IconButton(onClick = { /* redo */ }) {
                Icon(Icons.Default.Redo, contentDescription = "Redo")
            }
            IconButton(onClick = { /* restart */ }) {
                Icon(Icons.Default.Refresh, contentDescription = "Restart")
            }
            IconButton(onClick = { /* theme */ }) {
                Icon(Icons.Default.DarkMode, contentDescription = "Change Theme")
            }
            IconButton(onClick = { /* exit with save */ }) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Exit")
            }
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = "Timer",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    )
}