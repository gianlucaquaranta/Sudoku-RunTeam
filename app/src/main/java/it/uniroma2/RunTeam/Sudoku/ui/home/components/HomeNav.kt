package it.uniroma2.RunTeam.Sudoku.ui.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import it.uniroma2.RunTeam.Sudoku.LocalAppLocale
import it.uniroma2.RunTeam.Sudoku.R
import it.uniroma2.RunTeam.Sudoku.navigation.NavRoutes

@Composable
fun HomeNav(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val localeState = LocalAppLocale.current
    val context = LocalContext.current

    NavigationBar(
    )  {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                )
            },
            label = {
                Text(
                    "Home",
                )
            },
            selected = currentRoute == NavRoutes.HOME,
            onClick = { onNavigate(NavRoutes.HOME) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "Statistics",
                )
            },
            label = {
                Text(
                    context.getString(R.string.statistics),
                )
            },
            selected = currentRoute == NavRoutes.STATISTICS,
            onClick = { onNavigate(NavRoutes.STATISTICS) },
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Language",
                )
            },
            label = {
                Text(
                    context.getString(R.string.language),
                )
            },
            selected = currentRoute == NavRoutes.LANGUAGE,
            onClick = { onNavigate(NavRoutes.LANGUAGE) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.ColorLens,
                    contentDescription = "Theme",
                )
            },
            label = {
                Text(
                    context.getString(R.string.settings),
                )
            },
            selected = currentRoute == NavRoutes.SETTINGS,
            onClick = { onNavigate(NavRoutes.SETTINGS) }
        )
    }
}




