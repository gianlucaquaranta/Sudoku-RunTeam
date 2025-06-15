package it.uniroma2.RunTeam.Sudoku.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import it.uniroma2.RunTeam.Sudoku.R
import it.uniroma2.RunTeam.Sudoku.model.Difficulty

@Composable
fun DifficultySelectionDialog(
    onDismissRequest: () -> Unit,
    onDifficultySelected: (Difficulty) -> Unit
) {

    val context = LocalContext.current

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = context.getString(R.string.difficulty_choice),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onDifficultySelected(Difficulty.EASY) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(context.getString(R.string.difficulty_easy))
                }
                Button(
                    onClick = { onDifficultySelected(Difficulty.MEDIUM) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(context.getString(R.string.difficulty_medium))
                }
                Button(
                    onClick = { onDifficultySelected(Difficulty.HARD) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(context.getString(R.string.difficulty_hard))
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onDismissRequest) {
                    Text(context.getString(R.string.back_button))
                }
            }
        }
    }
}