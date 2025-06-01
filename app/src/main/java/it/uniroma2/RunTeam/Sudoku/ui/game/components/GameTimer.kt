package it.uniroma2.RunTeam.Sudoku.ui.game.components


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay



@Composable
fun GameTimer(
    modifier: Modifier = Modifier,
    isRunning: Boolean = true,
    onTick: ((Int) -> Unit)? = null
) {
    var seconds by remember { mutableStateOf(0) }
    //il metodo LaunchedEffect dovrà essere messo nella GameScreen perchè questa funzione dara quella che fa partire il timer (non grafico)
    //e dentro questa funzione si dovrà mettere il GameTimer(grafica)che cambiera leggermente la sua classe con GameTimer(seond:Int)
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (true) {
                delay(1000)
                seconds++
                onTick?.invoke(seconds)
            }
        }
    }

    val minutesPart = seconds / 60
    val secondsPart = seconds % 60
    val timeFormatted = String.format("%02d:%02d", minutesPart, secondsPart)

    Text(
        text = "$timeFormatted",
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge
    )
}

@Preview(showBackground = true)
@Composable
fun GameTimerPreview() {
    MaterialTheme {
        GameTimer(
            isRunning = true// Fermo il timer per la preview
        )
    }
}
