package it.uniroma2.RunTeam.Sudoku

import android.app.AlertDialog
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width

// Per le icone e il tema Material
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.foundation.layout.Spacer

// Per LazyVerticalGrid (se non già importato)
import androidx.compose.foundation.lazy.grid.GridCells

// Compose Foundation
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.foundation.shape.RoundedCornerShape

// Compose Material
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

// Compose Runtime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue


// Comppose UI
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton



@Composable
fun SudokuScreen(game: SudokuGame = remember { SudokuGame() }) {

    val hasWon by game.hasWon
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Griglia 9x9
        SudokuGrid(game = game)

        // Tastierino numerico
        NumberPad(onNumberSelected = { game.setNumber(it) })
    }


    Box(modifier = Modifier.fillMaxSize()) {
        // Il tuo layout esistente
        Column {
            SudokuGrid(game)
            NumberPad(onNumberSelected = { game.setNumber(it) })
        }

        // Mostra il dialog quando vinci
        if (hasWon) {
            WinDialog(
                onDismiss = { game.hasWon.value = false },
                onNewGame = {
                    game.generateNewPuzzle()
                    game.hasWon.value = false
                }
            )
        }
    }

}

@Composable
fun WinDialog(
    onDismiss: () -> Unit,
    onNewGame: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Complimenti!") },
        text = { Text("Hai completato il Sudoku con successo!") },
        confirmButton = {
            Button(onClick = onNewGame) {
                Text("Nuova partita")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Continua")
            }
        }
    )
}

@Composable
fun SudokuGrid(game: SudokuGame) {
    val grid by game.grid
    val selectedCell by game.selectedCell

    LazyVerticalGrid(
        columns = GridCells.Fixed(9),
        modifier = Modifier.padding(16.dp)
    ) {
        items(9 * 9) { index ->
            val row = index / 9
            val col = index % 9
            SudokuCell(
                number = grid[row][col],
                isSelected = selectedCell == Pair(row, col),
                row = row,  // Aggiunto
                col = col,  // Aggiunto
                onClick = { game.selectCell(row, col) }
            )
        }
    }
}

@Composable
fun SudokuCell(
    number: Int,
    isSelected: Boolean,
    row: Int,  // Aggiunto parametro row
    col: Int,  // Aggiunto parametro col
    onClick: () -> Unit
) {
    // Determina se è un bordo di blocco (3x3)
    val isBlockBorder = row % 3 == 0 || col % 3 == 0

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(1.dp)
            .background(
                if (isSelected) Color.LightGray.copy(alpha = 0.5f)
                else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                width = if (isBlockBorder) 2.dp else 1.dp,
                color = if (isBlockBorder) Color.Black else Color.Gray,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onClick() },
        contentAlignment = Center
    ) {
        Text(
            text = if (number != 0) "$number" else "",
            fontSize = 24.sp,
            color = if (isSelected) Color.Red else Color.Black
        )
    }
}

@Composable
fun NumberPad(onNumberSelected: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Titolo centrato
        Text(
            "Seleziona numero",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp),
            textAlign = TextAlign.Center // Aggiunto allineamento testo
        )

        // Griglia 3x3 centrata
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.width(200.dp),  // Adatta alla larghezza contenuto
            horizontalArrangement = Arrangement.Center // Centra orizzontalmente
        ) {
            items(9) { num ->
                NumberButton(
                    number = num + 1,
                    onClick = { onNumberSelected(num + 1) }
                )
            }
        }

        // Tasto Cancella centrato
        OutlinedButton(
            onClick = { onNumberSelected(0) },
            modifier = Modifier
                .padding(top = 12.dp)
                .height(48.dp)
                .align(Alignment.CenterHorizontally) // Forza centratura
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Cancella",
                    modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Cancella")
            }
        }
    }
}


@Composable
fun NumberButton(number: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(6.dp) // Padding uniforme
            .size(72.dp), // Dimensione fissa quadrata
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            contentAlignment = Center, // Centra il contenuto
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClick() }
        ) {
            Text(
                text = "$number",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center // Doppia assicurazione
            )
        }
    }
}


