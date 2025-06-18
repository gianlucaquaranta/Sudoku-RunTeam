package it.uniroma2.RunTeam.Sudoku.ui.settings


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import it.uniroma2.RunTeam.Sudoku.R
import it.uniroma2.RunTeam.Sudoku.ui.settings.components.ThemeOptionItem
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController,modifier: Modifier = Modifier) {

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.sel_theme)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ThemeOptionItem(title = context.getString(R.string.sel_theme_dark))
            ThemeOptionItem(title = context.getString(R.string.sel_theme_light))
            ThemeOptionItem(title = context.getString(R.string.sel_theme_jungle))
        }
    }
}
