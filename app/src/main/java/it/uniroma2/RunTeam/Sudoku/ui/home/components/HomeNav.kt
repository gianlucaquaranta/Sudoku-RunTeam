package it.uniroma2.RunTeam.Sudoku.ui.home.components

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import it.uniroma2.RunTeam.Sudoku.LocalAppLocale
import it.uniroma2.RunTeam.Sudoku.R
import it.uniroma2.RunTeam.Sudoku.navigation.NavRoutes
import it.uniroma2.RunTeam.Sudoku.updateLocale

@Composable
fun HomeNav(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val localeState = LocalAppLocale.current
    val context = LocalContext.current

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == NavRoutes.HOME,
            onClick = { onNavigate(NavRoutes.HOME) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.BarChart, contentDescription = "Statistiche") },
            label = { Text(text = context.getString(R.string.statistics)) },
            selected = currentRoute == NavRoutes.STATISTICS,
            onClick = { onNavigate(NavRoutes.STATISTICS) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Language, contentDescription = "Language") },
            label = { Text(text = context.getString(R.string.language)) },
            selected = currentRoute == NavRoutes.LANGUAGE,
            onClick = { onNavigate(NavRoutes.LANGUAGE) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text(text = context.getString(R.string.settings)) },
            selected = currentRoute == NavRoutes.SETTINGS,
            onClick = { onNavigate(NavRoutes.SETTINGS) } // placeholder
        )
    }
}

