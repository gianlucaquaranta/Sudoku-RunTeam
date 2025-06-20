package it.uniroma2.RunTeam.Sudoku.ui.home.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TitleSection(
    isTablet: Boolean,
    fontSize: TextUnit = 32.sp // default
) {
    Text(
        text = "SUDOKU GAME",
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.headlineLarge
    )
}
