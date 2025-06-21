package it.uniroma2.RunTeam.Sudoku.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import it.uniroma2.RunTeam.Sudoku.model.AppTheme

@Composable
fun SudokuTheme(
    theme: AppTheme = AppTheme.LIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = when (theme) {
        AppTheme.LIGHT -> lightColorScheme()
        AppTheme.DARK -> darkColorScheme()
        AppTheme.JUNGLE -> jungleLightColorScheme()
        AppTheme.BLUE -> oceanLightColorScheme()
        AppTheme.AURORA_DARK -> auroraLightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}