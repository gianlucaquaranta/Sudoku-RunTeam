package it.uniroma2.RunTeam.Sudoku.ui.game.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.uniroma2.RunTeam.Sudoku.model.Cell
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameTimerViewModel : ViewModel() {
    var seconds by mutableStateOf(0)
        private set

    lateinit var sudokuGrid: List<List<Cell>>
    private val undoStack = mutableStateListOf<Cell>()
    private val redoStack = mutableStateListOf<Cell>()

    private var timerJob: Job? = null

    fun startTimer() {
        if (timerJob == null) {
            timerJob = viewModelScope.launch {
                while (true) {
                    delay(1000)
                    seconds++
                }
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun resetTimer() {
        stopTimer()
        seconds = 0
    }

    fun updateCellValue(cell: Cell, newValue: Int) {
        val oldValue = cell.value
        if (oldValue != newValue) {
            undoStack.add(Cell(cell.row, cell.col, cell.isStartingCell, cell.initialValue, oldValue, newValue))
            redoStack.clear()
            cell.updateValue(newValue)
        }
    }

    fun clearCellValue(cell: Cell) {
        val oldValue = cell.value
        if (oldValue != 0) {
            undoStack.add(Cell(cell.row, cell.col, cell.isStartingCell, cell.initialValue, oldValue, 0))
            redoStack.clear()
            cell.clearValue()
        }
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val change = undoStack.removeAt(undoStack.lastIndex) // Modifica qui
            val cell = sudokuGrid[change.row][change.col]
            redoStack.add(change.copy(oldValue = change.newValue ?: 0, newValue = change.oldValue ?: 0))
            cell.updateValue(change.oldValue ?: 0) // Modifica qui
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val change = redoStack.removeAt(redoStack.lastIndex) // Modifica qui
            val cell = sudokuGrid[change.row][change.col]
            // Simile a undo, assicurati che i valori non siano nulli.
            undoStack.add(change.copy(oldValue = change.newValue ?: 0, newValue = change.oldValue ?: 0))
            // Assicurati che change.newValue non sia nullo.
            cell.updateValue(change.oldValue ?: 0) // Modifica qui
        }
    }

}