package it.uniroma2.RunTeam.Sudoku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import it.uniroma2.RunTeam.Sudoku.ui.theme.SudokuRunteamTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SudokuRunteamTheme { // Sostituisci con il tuo tema
                SudokuScreen()
            }
        }
    }
}