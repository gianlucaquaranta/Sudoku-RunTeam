package it.uniroma2.RunTeam.Sudoku.ui.home.components

import android.content.Context
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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

    NavigationBar(
        modifier = Modifier.height(56.dp)
        .clip(RoundedCornerShape(20.dp))
    )  {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier
                        .padding(top = 15.dp)
                )
            },
            label = {
                Text(
                    "Home",
                    modifier = Modifier.padding(bottom =0.dp), // sposta il testo leggermente in alto
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
                    modifier = Modifier
                        .padding(top = 15.dp) // sposta l'icona leggermente in basso
                )
            },
            label = {
                Text(
                    context.getString(R.string.statistics),
                    modifier = Modifier.padding(bottom = 0.dp), // sposta il testo leggermente in alto
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
                    modifier = Modifier
                        .padding(top = 15.dp) // sposta l'icona leggermente in basso
                )
            },
            label = {
                Text(
                    context.getString(R.string.language),
                    modifier = Modifier.padding(bottom = 0.dp), // sposta il testo leggermente in alto
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
                    modifier = Modifier
                        .padding(top = 15.dp) // sposta l'icona leggermente in basso
                )
            },
            label = {
                Text(
                    context.getString(R.string.settings),
                    modifier = Modifier.padding(bottom = 0.dp), // sposta il testo leggermente in alto
                )
            },
            selected = currentRoute == NavRoutes.SETTINGS,
            onClick = { onNavigate(NavRoutes.SETTINGS) }
        )
    }
}




