package it.uniroma2.RunTeam.Sudoku.ui.game.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SudokuGrid() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (i in 0 until 9) {
            Row(modifier = Modifier.wrapContentWidth()) {
                for (j in 0 until 9) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .border(1.dp, Color.Gray)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SudokuGridPreview() {
    SudokuGrid()
}