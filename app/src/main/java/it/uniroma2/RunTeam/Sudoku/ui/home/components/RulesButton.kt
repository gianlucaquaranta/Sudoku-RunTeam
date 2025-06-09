package it.uniroma2.RunTeam.Sudoku.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.uniroma2.RunTeam.Sudoku.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

@Composable
fun RulesButton(onClick: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    Button(onClick = { showDialog = true }) {
        Text("Rules")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {},
            dismissButton = {},
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Sudoku Rules",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    IconButton(onClick = { showDialog = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            },
            text = {
                Text(
                    text = """
                        • Each row must contain the digits from 1 to 9 without repetition.
                        • Each column must contain the digits from 1 to 9.
                        • Each of the 9 sub-grids (3x3) must also contain all digits from 1 to 9.
                        • Use logic and deduction to solve the puzzle.
                        • No guessing — pure strategy and patience!
                    """.trimIndent(),
                    fontSize = 16.sp
                )
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}
