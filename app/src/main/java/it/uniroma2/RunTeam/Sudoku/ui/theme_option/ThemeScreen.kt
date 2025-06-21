package it.uniroma2.RunTeam.Sudoku.ui.theme_option


import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import it.uniroma2.RunTeam.Sudoku.R
import it.uniroma2.RunTeam.Sudoku.ui.theme_option.components.ThemeOptionItem
import androidx.navigation.NavController
import it.uniroma2.RunTeam.Sudoku.model.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onThemeSelected: (AppTheme) -> Unit
) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val isTablet = configuration.screenWidthDp >= 600
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isPhoneLandscape = !isTablet && isLandscape

    val scrollState = rememberScrollState()

    Scaffold(
    ) { innerPadding ->
        val topSpace = if (isPhoneLandscape) 10.dp else 50.dp//distanza da topbar a inzio card
        val horizontalSpace = if (isPhoneLandscape) 20.dp else 10.dp
        val cardSpacing = if (isPhoneLandscape) 12.dp else 16.dp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding() + topSpace,
                    bottom = innerPadding.calculateBottomPadding(),
                    start = horizontalSpace,
                    end = horizontalSpace
                )
                .then(
                    if (!isPhoneLandscape) {
                        Modifier
                            .verticalScroll(scrollState)
                            .padding(bottom = 90.dp)
                    } else {
                        Modifier
                    }
                ),
            verticalArrangement = Arrangement.spacedBy(cardSpacing, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ThemeOptionItem(
                title = context.getString(R.string.sel_theme_dark),
                onClick = { onThemeSelected(AppTheme.DARK) },modifier = Modifier
                    .fillMaxWidth(),
                flag ="scuro",
            )
            ThemeOptionItem(
                title = context.getString(R.string.sel_theme_light),
                onClick = { onThemeSelected(AppTheme.LIGHT) },modifier = Modifier
                    .fillMaxWidth(),
                flag = "chiaro"
            )
            ThemeOptionItem(
                title = context.getString(R.string.sel_theme_jungle),
                onClick = { onThemeSelected(AppTheme.JUNGLE) },modifier = Modifier
                    .fillMaxWidth(),
                flag = "jungle"
            )
            ThemeOptionItem(
                title = context.getString(R.string.sel_theme_blue),
                onClick = { onThemeSelected(AppTheme.BLUE) },modifier = Modifier
                    .fillMaxWidth(),
                flag = "blue"
            )
            ThemeOptionItem(
                title = context.getString(R.string.sel_theme_auroraDark),
                onClick = { onThemeSelected(AppTheme.AURORA_DARK) },modifier = Modifier
                    .fillMaxWidth(),
                flag = "aurora"
            )

        }
    }
}

