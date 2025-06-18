package it.uniroma2.RunTeam.Sudoku

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import it.uniroma2.RunTeam.Sudoku.navigation.SudokuNavHost
import it.uniroma2.RunTeam.Sudoku.ui.theme.SudokuTheme
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context?) {
        val prefs = newBase?.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val lang = prefs?.getString("language", Locale.getDefault().language) ?: "en"
        val locale = Locale(lang)
        val config = Configuration()
        config.setLocale(locale)
        val localizedContext = newBase?.createConfigurationContext(config)
        super.attachBaseContext(localizedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recupero delle preferenze salvate
        val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val savedThemeIsDark = prefs.getBoolean("dark_theme", false)

        setContent {
            // Stato che tiene traccia del tema attivo
            var isDarkTheme by rememberSaveable { mutableStateOf(savedThemeIsDark) }

            // Ogni volta che cambia, lo salviamo
            LaunchedEffect(isDarkTheme) {
                prefs.edit().putBoolean("dark_theme", isDarkTheme).apply()
            }

            // Applica il tema corretto
            SudokuTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                SudokuNavHost(
                    onThemeChange = { isDarkTheme = it },
                    navController = navController
                )
            }
        }
    }
}
