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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar(seconds:Int) {

    TopAppBar(
        title = { Text("ICON") },
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

@Preview(showBackground = true)
@Composable
fun GameTopBarPreview() {
    GameTopBar(125)
}