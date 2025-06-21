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
import it.uniroma2.RunTeam.Sudoku.model.AppTheme
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

        val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)

        // Legge dal SharedPreferences il nome dell'AppTheme salvato
        val savedThemeName = prefs.getString("app_theme", AppTheme.LIGHT.name) ?: AppTheme.LIGHT.name
        val savedTheme = AppTheme.valueOf(savedThemeName)

        setContent {
            var currentTheme by rememberSaveable { mutableStateOf(savedTheme) }

            LaunchedEffect(currentTheme) {
                prefs.edit().putString("app_theme", currentTheme.name).apply()
            }

            SudokuTheme(theme = currentTheme) {
                val navController = rememberNavController()
                SudokuNavHost(
                    onThemeChange = { newTheme -> currentTheme = newTheme },
                    navController = navController
                )
            }
        }
    }
}

