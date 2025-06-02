package it.uniroma2.RunTeam.Sudoku.ui.home.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RulesButton(onClick: () -> Unit = {}) {
    Button(
        onClick = onClick
    ) {
        Text("Game Rules")
    }
}

@Preview(showBackground = true)
@Composable
fun RulesButtonPreview() {
    RulesButton(onClick = {})
}