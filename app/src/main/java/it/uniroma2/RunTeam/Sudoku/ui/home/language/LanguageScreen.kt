package it.uniroma2.RunTeam.Sudoku.ui.home.language

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import it.uniroma2.RunTeam.Sudoku.MainActivity
import it.uniroma2.RunTeam.Sudoku.ui.home.language.components.LanguageOption
import it.uniroma2.RunTeam.Sudoku.R
import androidx.compose.ui.res.stringResource
import java.util.Locale
import it.uniroma2.RunTeam.Sudoku.LocalAppLocale
import androidx.core.content.edit
import it.uniroma2.RunTeam.Sudoku.ui.home.components.HomeNav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(navController: NavController,modifier: Modifier = Modifier) {
    val localeState = LocalAppLocale.current
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.sel_language)) },
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
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LanguageOption(
                languageResId = R.string.italian,
                flag = "🇮🇹",
                onClick = {
                    context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
                        .edit { putString("language", "it") }
                    localeState.value = Locale("it")
                }
            )

            LanguageOption(
                languageResId = R.string.english,
                flag = "🇬🇧",
                onClick = {
                    context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
                        .edit { putString("language", "en") }
                    localeState.value = Locale("en")
                }
            )
        }
    }
}




