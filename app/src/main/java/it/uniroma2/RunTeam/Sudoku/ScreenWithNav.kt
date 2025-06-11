package it.uniroma2.RunTeam.Sudoku

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import it.uniroma2.RunTeam.Sudoku.ui.home.components.HomeNav

@Composable
fun ScreenWithNav(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        bottomBar = {
            HomeNav(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        },
        content = content
    )
}
