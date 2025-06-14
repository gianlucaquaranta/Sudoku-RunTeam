package it.uniroma2.RunTeam.Sudoku.ui.game.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.uniroma2.RunTeam.Sudoku.R

@Composable
fun SuggestionButton() {

    val context = LocalContext.current

    Button(onClick = { /* request suggestion */ }, modifier = Modifier.padding(8.dp)) {
        Text(context.getString(R.string.suggestion_button))
    }
}
