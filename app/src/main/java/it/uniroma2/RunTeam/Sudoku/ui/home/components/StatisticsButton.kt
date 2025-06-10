package it.uniroma2.RunTeam.Sudoku.ui.home.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun StatisticsButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Statistics")
    }
}
