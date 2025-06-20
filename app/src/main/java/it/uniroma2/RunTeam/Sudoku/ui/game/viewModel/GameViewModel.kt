package it.uniroma2.RunTeam.Sudoku.ui.game.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.uniroma2.RunTeam.Sudoku.database.entity.SavedGame
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
import it.uniroma2.RunTeam.Sudoku.database.repository.SavedGameRepository
import it.uniroma2.RunTeam.Sudoku.database.AppDatabase
import it.uniroma2.RunTeam.Sudoku.database.dto.CellDto
import it.uniroma2.RunTeam.Sudoku.database.dto.SudokuGridDto
import it.uniroma2.RunTeam.Sudoku.database.entity.GameStats
import it.uniroma2.RunTeam.Sudoku.database.repository.GameStatsRepository

import kotlin.random.Random

class GameViewModel(application: Application) : ViewModel() {

    private val db = AppDatabase.getDatabase(application)
    private val savedGameRepository = SavedGameRepository(db.savedGameDao())
    private val gameStatsRepository = GameStatsRepository(db.gameStatsDao())

    private val _uiState = MutableStateFlow(GameState()) //attributo privato per mantenere lo stato di gioco, modificabile perché Mutable ma solo da viewmodel
    val uiState: StateFlow<GameState> = _uiState.asStateFlow() //Pubblico ma read-only, serve alla UI per leggere lo stato del gioco

    private val undoStack = mutableStateListOf<Cell>()
    private val redoStack = mutableStateListOf<Cell>()

    var isResumedGame: Boolean = false
    private var timerJob: Job? = null
    private var initialGrid: SudokuGrid? = null
    private var solutionGrid: SudokuGrid? = null
    var maxErrors: Int = 0

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

    //fun resetTimer() {
    //    stopTimer()
    //    _uiState.update { it.copy(secondsElapsed = 0) }
    //}

    fun createGrid(context: Context, difficulty: Difficulty) {
        isResumedGame = false
        _uiState.update { it.copy(isLoading = true) }
        SudokuGenerator(context).generateSudoku(
            difficulty = difficulty,
            onSuccess = { (puzzle, solution) -> initializeGame(puzzle, solution) },
            onError = { errorMessage ->
                Log.e("GameViewModel", "Errore durante il fetch del sudoku: $errorMessage")
            }
        )
    }

    suspend fun tryResumeGame(): Difficulty? {
        try {
            isResumedGame = true
            val existingGame = savedGameRepository.getGameById()
            val lastGame = existingGame?.lastOrNull()

            if (existingGame != null) {
                // Converte i DTO in SudokuGrid "reali" con state per la UI
                val currentGrid = lastGame?.currentGridState?.toSudokuGrid()
                val solutionGrid = lastGame?.solutionGridState?.toSudokuGrid()
                val difficultyGame = solutionGrid?.difficulty

                if (lastGame != null) {
                    this.solutionGrid = solutionGrid
                    this.initialGrid = currentGrid
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sudokuGrid = currentGrid,
                            solutionGridState = solutionGrid,
                            errors = lastGame.remainingErrors,
                            secondsElapsed = lastGame.elapsedTimeInSeconds,
                            remainingHints = lastGame.remainingHints,
                            isGameCompleted = false,
                            isGameLost = false
                        )
                    }
                }
                maxErrors = when(difficultyGame){
                    Difficulty.EASY -> { 5 }
                    Difficulty.MEDIUM -> { 3 }
                    Difficulty.HARD -> { 2 }
                    null -> { 5 }
                }
                startTimer()
                return difficultyGame
            } else {
                _uiState.update { it.copy(isLoading = false) }
                return null
            }
        } catch (e: Exception) {
            Log.e("GameViewModel", "Errore durante il resume della partita: ${e.message}", e)
            _uiState.update { it.copy(isLoading = false) }
            return null
        }
    }

    private fun initializeGame(puzzle: SudokuGrid, solution: SudokuGrid){
        solutionGrid = solution

        val newGrid: Array<IntArray> = Array(9) { row ->
            IntArray(9) { col ->
                puzzle.getCell(row,col)?.value ?: 0
            }
        }
        //Si crea una copia del puzzle per evitare che il sudoku mantenuto in GameState e l'attributo initialGrid condividano il riferimento alla stessa istanza
        val puzzleCopy: SudokuGrid = SudokuGrid.fromValues(newGrid, puzzle.difficulty)

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
            Difficulty.HARD -> {
                maxErrors = 2
                _uiState.update { it.copy(remainingHints = 2) }
            }
            null -> Log.e("GameViewModel", "Difficulty null")
        }
        val hintsNum = _uiState.value.remainingHints
        Log.d("GameViewModel", "$hintsNum")
        /*si riportano ai valori iniziali tutti i valori dello stato di gioco, poiché
        si potrebbe passare da questo metodo sia all'inizio di una partita, sia quando l'utente
        conclude una partita e seleziona "Nuova partita", dunque è necessario resettare lo stato
         */
        _uiState.update {
            it.copy(
                isLoading = false,
                sudokuGrid = puzzleCopy,
                selectedCell = null,
                //errors = it.errors,
                errors = 0,
                //remainingHints = hintsNum,
                isNoteMode = false,
                secondsElapsed = 0,
                isGameCompleted = false,
                isGameLost = false
            )
        }
        undoStack.clear()// reset di undo/redo
        redoStack.clear()
        startTimer()
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
        if( _uiState.value.selectedCell == null || !_uiState.value.selectedCell?.isValid!!) _uiState.update { it.copy(isNoteMode = !it.isNoteMode) }
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
    }

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

    fun hint(){
        val result = SudokuSuggester.getHint(_uiState.value.sudokuGrid!!)
        Log.d("Hint", "Result: ${result.toString()}")
        if(result != null){
            val toChange = _uiState.value.sudokuGrid?.getCell(result.row, result.col)
            updateCellValue(toChange!!, result.value)
            _uiState.value.selectedCell = toChange
        } else {
            val emptyCells = mutableListOf<Cell>()
            for (row in 0..8) {
                for (col in 0..8) {
                    _uiState.value.sudokuGrid?.getCell(row, col)?.let { cell ->
                        if (cell.value == 0) {
                            emptyCells.add(cell)
                        }
                    }
                }
            }

            val randomCell = emptyCells[Random.nextInt(emptyCells.size)]
            val rightCell = solutionGrid?.getCell(randomCell.row, randomCell.col)

            updateCellValue(randomCell, rightCell?.value ?: 0)

            _uiState.value.selectedCell = randomCell

        }
        _uiState.value.remainingHints--
        val remainingHints = uiState.value.remainingHints
        Log.d("GameViewModel", "remaining hints post-decremento: $remainingHints")
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val change = undoStack.removeAt(undoStack.lastIndex) // Modifica qui
            val cell = _uiState.value.sudokuGrid?.getCell(change.row, change.col)
            redoStack.add(change.copy(oldValue = change.newValue ?: 0, newValue = change.oldValue ?: 0))
            cell?.unvalidate() //Per far sì che si possa rendere ricliccabile e rimodificabile una cella che era valida prima dell'undo
            cell?.cleanIncorrect()
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
            //updateCellValue(cell!!, change.oldValue ?: 0)

            cell?.updateValue(change.oldValue ?: 0) // Modifica qui
            if(cell != null && checkCell(change.row, change.col)) {
                cell.validate()
            }
        }
    }

    fun restartGame() {
        if (isResumedGame) {
            restartGameAfterResume()
        } else {
            initializeGame(initialGrid!!, solutionGrid!!)
        }
        _uiState.update { it.copy(isGameCompleted = false, isGameLost = false) }
    }

    fun restartGameAfterResume() {
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
                    currentCell.isIncorrect = false
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
                isNoteMode = false, // Eventualmente resetta la modalità note
                errors = it.errors,
                remainingHints = it.remainingHints
            )
        }
        // Pulisci gli stack di undo e redo
        undoStack.clear()
        redoStack.clear()
        Log.d("GameViewModel", "Game restarted.")
        return
    }

    fun onSaveExit() {
        viewModelScope.launch {
            Log.d("DB is open: ", db.isOpen.toString())
            val currentState = _uiState.value
            val currentGrid = currentState.sudokuGrid
            val currentSolutionGrid = solutionGrid

            if (currentGrid != null && currentSolutionGrid != null) {
                val remainingErrors = currentState.errors
                val remainingHints = currentState.remainingHints

                val gameToSave = SavedGame(
                    currentGridState = currentGrid.toDto(),
                    solutionGridState = currentSolutionGrid.toDto(),
                    remainingErrors = remainingErrors,
                    remainingHints = remainingHints,
                    elapsedTimeInSeconds = currentState.secondsElapsed,
                )

                try {
                    savedGameRepository.insertGame(gameToSave)
                    Log.d("GameViewModel", "Partita salvata con successo con ID: ${gameToSave.id}")
                } catch (e: Exception) {
                    Log.e("GameViewModel", "Errore durante il salvataggio della partita: ${e.javaClass.simpleName} - ${e.message}", e)
                }

                _shouldNavigateHome.value = true
            } else {
                Log.w("GameViewModel", "Impossibile salvare la partita: griglia corrente o soluzione mancante.")
                _shouldNavigateHome.value = true
            }
        }
    }

    fun updateGameStatsInternal(
        difficulty: Difficulty,
        isWin: Boolean
    ) {
        viewModelScope.launch {
            val existingStats = gameStatsRepository.getStatsByDifficulty(difficulty)
            val currentTime = _uiState.value.secondsElapsed

            val newStats: GameStats = if (existingStats != null) {
                // Statistiche esistenti, aggiornale
                val newBestTime = if (isWin && (existingStats.bestTimeSeconds == 0 || currentTime < existingStats.bestTimeSeconds)) {
                    currentTime
                } else {
                    existingStats.bestTimeSeconds
                }
                existingStats.copy(
                    bestTimeSeconds = newBestTime, // Aggiorna solo se è una vittoria e un tempo migliore
                    wonGames = existingStats.wonGames + if (isWin) 1 else 0,
                    lostGames = existingStats.lostGames + if (!isWin) 1 else 0,
                    totalTimePlayedSeconds = existingStats.totalTimePlayedSeconds + currentTime
                )
            } else {
                // Nessuna statistica esistente, creane una nuova
                GameStats(
                    difficulty = difficulty,
                    bestTimeSeconds = if (isWin) currentTime else 0, // Imposta il tempo migliore solo se è una vittoria
                    wonGames = if (isWin) 1 else 0,
                    lostGames = if (!isWin) 1 else 0,
                    totalTimePlayedSeconds = currentTime
                )
            }
            gameStatsRepository.saveGameStats(newStats)
        }
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


    //Metodi conversione
    fun Cell.toDto(): CellDto = CellDto(
        row = row,
        col = col,
        isStartingCell = isStartingCell,
        initialValue = initialValue,
        oldValue = oldValue,
        newValue = newValue,
        value = value,
        isSelected = isSelected,
        isIncorrect = isIncorrect,
        isValid = isValid,
        notes = notes
    )

    fun SudokuGrid.toDto(): SudokuGridDto {
        val gridDto = grid.map { row -> row.map { it.toDto() } }
        return SudokuGridDto(grid = gridDto, difficulty = difficulty)
    }

    private fun SudokuGridDto.toSudokuGrid(): SudokuGrid {
        val convertedGrid = Array(9) { row ->
            Array(9) { col ->
                grid[row][col].toCell()
            }
        }
        return SudokuGrid(grid = convertedGrid, difficulty = difficulty)
    }

    private fun CellDto.toCell(): Cell = Cell(
        row = row,
        col = col,
        isStartingCell = isStartingCell,
        initialValue = initialValue,
        oldValue = oldValue,
        newValue = newValue
    ).apply {
        value = this@toCell.value
        isSelected = this@toCell.isSelected
        isIncorrect = this@toCell.isIncorrect
        isValid = this@toCell.isValid
        notes = this@toCell.notes
    }


}