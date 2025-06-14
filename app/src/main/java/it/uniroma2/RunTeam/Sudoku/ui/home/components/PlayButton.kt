package it.uniroma2.RunTeam.Sudoku.ui.home.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import it.uniroma2.RunTeam.Sudoku.R
import androidx.compose.ui.platform.LocalContext

@Composable
fun PlayButton(onClick: () -> Unit,modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Button(onClick = onClick,modifier=modifier) {
        Text(text = context.getString(R.string.play_button))
    }
}

