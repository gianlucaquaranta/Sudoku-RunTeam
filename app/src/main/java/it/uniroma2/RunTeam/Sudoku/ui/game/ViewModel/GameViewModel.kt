package it.uniroma2.RunTeam.Sudoku.ui.game.ViewModel

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.copy
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.uniroma2.RunTeam.Sudoku.model.Cell
import it.uniroma2.RunTeam.Sudoku.model.Difficulty
import it.uniroma2.RunTeam.Sudoku.model.SudokuGrid
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameState()) //attributo privato per mantenere lo stato di gioco, modificabile perché Mutable ma solo da viewmodel
    val uiState: StateFlow<GameState> = _uiState.asStateFlow() //Pubblico ma read-only, serve alla UI per leggere lo stato del gioco

    private val undoStack = mutableStateListOf<Cell>()
    private val redoStack = mutableStateListOf<Cell>()

    private var timerJob: Job? = null
    private var solutionGrid: SudokuGrid? = null

    fun startTimer() {
        if (timerJob == null) {
            timerJob = viewModelScope.launch {
                while (true) {
                    delay(1000)
                    _uiState.update { it.copy(secondsElapsed = it.secondsElapsed + 1) }
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
        _uiState.update { it.copy(secondsElapsed = 0) }
    }

    fun createGrid(context: Context, difficulty: Difficulty) {
        SudokuGenerator(context).generateSudoku(
            difficulty = difficulty,
            onSuccess = { (puzzle, solution) ->
                _uiState.value.sudokuGrid = puzzle
                solutionGrid = solution
            },
            onError = { errorMessage ->
                Log.e("GameViewModel", "Errore durante il fetch del sudoku: $errorMessage")
            }
        )
    }

    fun onCellSelected(cell: Cell) {
        if (!cell.isStartingCell) {
            _uiState.update {
                val selected = if (it.selectedCell == cell) null else cell
                it.copy(selectedCell = selected)
            }
        }
    }

    fun toggleNoteMode() {
        _uiState.update { it.copy(isNoteMode = !it.isNoteMode) }
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
            val cell = _uiState.value.sudokuGrid?.getCell(change.row, change.col)
            redoStack.add(change.copy(oldValue = change.newValue ?: 0, newValue = change.oldValue ?: 0))
            cell?.updateValue(change.oldValue ?: 0) // Modifica qui
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val change = redoStack.removeAt(redoStack.lastIndex) // Modifica qui
            val cell = _uiState.value.sudokuGrid?.getCell(change.row, change.col)
            // Simile a undo, assicurati che i valori non siano nulli.
            undoStack.add(change.copy(oldValue = change.newValue ?: 0, newValue = change.oldValue ?: 0))
            // Assicurati che change.newValue non sia nullo.
            cell?.updateValue(change.oldValue ?: 0) // Modifica qui
        }
    }

    fun restartGame() {
        val currentSudokuGrid = _uiState.value.sudokuGrid
        if (currentSudokuGrid == null) {
            Log.w("GameViewModel", "SudokuGrid is null, cannot restart. Potrebbe essere necessario creare una nuova griglia.")
            // Potresti voler richiamare createGrid qui se non c'è una griglia,
            // oppure semplicemente non fare nulla se non c'è una partita da riavviare.
            return
        }

        // Crea una nuova griglia basata su quella corrente,
        // resettando le celle modificabili al loro stato 'initialValue' (se presente) o a 0.
        val newBoardCells = Array(currentSudokuGrid.grid.size) { r ->
            Array(currentSudokuGrid.grid[r].size) { c ->
                val currentCell = currentSudokuGrid.grid[r][c]
                if (!currentCell.isStartingCell) {
                    // Se la cella non è una cella iniziale, resettala.
                    // Resetta al valore iniziale se lo hai, altrimenti a 0.
                    // Assumiamo che initialValue sia 0 per le celle vuote all'inizio
                    // o che `value` di una startingCell sia il suo valore fisso.
                    currentCell.copy()
                    currentCell.value = 0
                    currentCell.clearNotes()
                    currentCell
                } else {
                    // Le celle iniziali rimangono invariate.
                    // È importante restituire una copia se SudokuGrid o Cell sono data class
                    // per garantire che i cambiamenti vengano rilevati se si aggiorna l'intera struttura.
                    currentCell.copy() // O semplicemente currentCell se non è necessario creare copie per le starting cells
                }
            }
        }

        // Aggiorna lo stato della UI con la nuova griglia resettata
        _uiState.update {
            it.copy(
                sudokuGrid = currentSudokuGrid.copy(grid = newBoardCells),
                selectedCell = null, // Deseleziona qualsiasi cella
                isNoteMode = false // Eventualmente resetta la modalità note
            )
        }
        // Pulisci gli stack di undo e redo
        undoStack.clear()
        redoStack.clear()

        Log.d("GameViewModel", "Game restarted.")
    }


}