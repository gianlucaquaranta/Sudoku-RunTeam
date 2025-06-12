package it.uniroma2.RunTeam.Sudoku.ui.game.ViewModel

import android.content.Context
import android.util.Log
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
    private var initialGrid: SudokuGrid? = null
    private var solutionGrid: SudokuGrid? = null
    private var maxErrors: Int = 0

    private val _shouldNavigateHome = MutableStateFlow(false)
    val shouldNavigateHome: StateFlow<Boolean> = _shouldNavigateHome

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
        _uiState.update { it.copy(isLoading = true) }
        SudokuGenerator(context).generateSudoku(
            difficulty = difficulty,
            onSuccess = { (puzzle, solution) -> initializeGame(puzzle, solution) },
            onError = { errorMessage ->
                Log.e("GameViewModel", "Errore durante il fetch del sudoku: $errorMessage")
            }
        )
    }

    private fun initializeGame(puzzle: SudokuGrid, solution: SudokuGrid){
        solutionGrid = solution

        val newGrid: Array<IntArray> = Array(9) { row ->
            IntArray(9) { col ->
                puzzle.getCell(row,col)?.value ?: 0
            }
        }

        val puzzleCopy: SudokuGrid = SudokuGrid.fromValues(newGrid, puzzle.difficulty) //per non far condividere il riferimento alla stessa istanza sia a initialGrid che a al sudoku in _uiState

        initialGrid = puzzle

        when(solutionGrid?.difficulty){
            Difficulty.EASY -> {
                maxErrors = 5
                _uiState.update { it.copy(remainingHints = 5) }
            }
            Difficulty.MEDIUM -> {
                maxErrors = 3
                _uiState.update { it.copy(remainingHints = 3) }
            }
            Difficulty.DIFFICULT -> {
                maxErrors = 2
                _uiState.update { it.copy(remainingHints = 2) }
            }
            null -> Log.e("GameViewModel", "Difficulty null")
        }
        /*si riportano ai valori iniziali tutti i valori dello stato di gioco, poiché
        si potrebbe passare da questo metodo sia all'inizio di una partita, sia quando l'utente
        conclude una partita e seleziona "Nuova partita", dunque è necessario resettare lo stato
         */
        _uiState.update {
            it.copy(
                isLoading = false,
                sudokuGrid = puzzleCopy,
                selectedCell = null,
                errors = 0,
                isNoteMode = false,
                secondsElapsed = 0,
                isGameCompleted = false,
                isGameLost = false
            )
        }
        undoStack.clear()// ho fatto reset di undo/redo
        redoStack.clear()
    }

    private fun checkCell(row: Int, col: Int): Boolean {
        val currentValue = _uiState.value.sudokuGrid?.getCell(row, col)?.value
        val rightValue = solutionGrid?.getCell(row, col)?.value
        return currentValue == rightValue
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
        if(!_uiState.value.selectedCell?.isValid!!) _uiState.update { it.copy(isNoteMode = !it.isNoteMode) }
    }

    fun updateCellValue(cell: Cell, newValue: Int) {
        val oldValue = cell.value
        if (oldValue != newValue && !cell.isValid) { //una cella valida non deve poter essere modificabile
            undoStack.add(Cell(cell.row, cell.col, cell.isStartingCell, cell.initialValue, oldValue, newValue))
            redoStack.clear()
            cell.updateValue(newValue)
            if(!checkCell(cell.row,cell.col)){
                cell.markIncorrect()
                _uiState.value.errors ++
                if(_uiState.value.errors == maxErrors){
                    _uiState.update { it.copy(isGameLost = true) }
                }
            } else {
                cell.validate()
                checkGameCompletion()
            }
        }
        /*
        if(!checkCell(cell.row,cell.col)){
            cell.markIncorrect()
        } else {
            cell.validate()
            checkGameCompletion() // Aggiungi questo solo se è corretto,qui potremmo fare anche la logica degli errori
        }
                           //con un contatore ,oppure nell'if facciamo il contatore e se perde si crea un'altra fun
         */
    }                             //che gli esce un pop up che dice che ha perso

    fun clearCellValue(cell: Cell) {
        if(!_uiState.value.selectedCell?.isValid!!) {
            val oldValue = cell.value
            if (oldValue != 0) {
                undoStack.add(
                    Cell(cell.row, cell.col, cell.isStartingCell, cell.initialValue, oldValue, 0)
                )
                redoStack.clear()
                cell.clearValue()
            }
        }


    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val change = undoStack.removeAt(undoStack.lastIndex) // Modifica qui
            val cell = _uiState.value.sudokuGrid?.getCell(change.row, change.col)
            redoStack.add(change.copy(oldValue = change.newValue ?: 0, newValue = change.oldValue ?: 0))
            cell?.unvalidate() //Per far sì che si possa rendere ricliccabile e rimodificabile una cella che era valida prima dell'undo
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
            if(cell != null && checkCell(change.row, change.col)) {
                cell.validate()
            }
        }
    }

    fun restartGame() {
        initializeGame(initialGrid!!, solutionGrid!!)
    }

    fun oldRestartGame() {
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
                    currentCell.updateValue(0)
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

    fun onSaveExit(){
        //Andrea poi qua aggiungi quello che ti serve per salvare la partita che si sta giocando
        _shouldNavigateHome.value = true
    }

    fun resetNavigateHomeFlag() {
        _shouldNavigateHome.value = false
    }

    private fun checkGameCompletion() {
        val grid = _uiState.value.sudokuGrid ?: return

        for (row in 0 until grid.grid.size) {
            for (col in 0 until grid.grid[row].size) {
                val cell = grid.getCell(row, col)
                if (cell != null) {
                    if (cell.value == 0 || !checkCell(row, col)) {
                        return // Qualcosa è sbagliato o incompleto
                    }
                }
            }
        }

        // Tutte le celle sono piene e corrette
        _uiState.update { it.copy(isGameCompleted = true) }
    }

}