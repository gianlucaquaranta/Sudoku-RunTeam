package it.uniroma2.RunTeam.Sudoku

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

        setContent {
            SudokuTheme {
                val navController = rememberNavController()
                SudokuNavHost(navController)
            }
        }
    }
}