package it.uniroma2.RunTeam.Sudoku.ui.language

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.uniroma2.RunTeam.Sudoku.ui.language.components.LanguageOption
import it.uniroma2.RunTeam.Sudoku.R
import java.util.Locale
import it.uniroma2.RunTeam.Sudoku.LocalAppLocale
import androidx.core.content.edit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(navController: NavController, modifier: Modifier = Modifier) {
    val localeState = LocalAppLocale.current
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isPhoneLandscape = !isTablet && isLandscape

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.sel_language)) },
                modifier = Modifier.height(if (isPhoneLandscape) 48.dp else 56.dp),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        // Calcolo dinamico degli spazi
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
                ),
            verticalArrangement = Arrangement.spacedBy(cardSpacing, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LanguageOption(
                languageResId = R.string.italian,
                flag = "🇮🇹",
                onClick = {
                    context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
                        .edit { putString("language", "it") }
                    localeState.value = Locale("it")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isPhoneLandscape) 60.dp else 72.dp)
            )

            LanguageOption(
                languageResId = R.string.english,
                flag = "🇬🇧",
                onClick = {
                    context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
                        .edit { putString("language", "en") }
                    localeState.value = Locale("en")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isPhoneLandscape) 60.dp else 72.dp)
            )
        }
    }
}



