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
import it.uniroma2.RunTeam.Sudoku.database.entity.GameStats
import it.uniroma2.RunTeam.Sudoku.database.repository.GameStatsRepository
import it.uniroma2.RunTeam.Sudoku.ui.game.ViewModel.GameState

class GameViewModel(application: Application) : ViewModel() {

    private val db = AppDatabase.getDatabase(application) // Corretto uso di application context
    private val savedGameRepository = SavedGameRepository(db.savedGameDao())
    private val gameStatsRepository = GameStatsRepository(db.gameStatsDao())

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

    suspend fun tryResumeGame() : Boolean{
        try {
            // Supponiamo che savedGameRepository.getGameById(1) restituisca un oggetto SavedGame
            // che contiene tutti i dati necessari, inclusa la difficoltà, il tempo trascorso, ecc.
            val existingGame: SavedGame? = savedGameRepository.getGameById(1) // Il tuo ID di gioco

            if (existingGame != null) {
                // Qui avviene la "ricreazione" popolando lo stato
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        sudokuGrid = existingGame.currentGridState, // Assicurati che SudokuGrid sia serializzabile/deserializzabile correttamente
                        solutionGridState = existingGame.solutionGridState, // Anche questo
                        errors = existingGame.remainingErrors, // O `totalErrorsMadeSoFar` a seconda di come lo chiami
                        secondsElapsed = existingGame.elapsedTimeInSeconds,
                        remainingHints = existingGame.remainingHints,
                    )
                }
                return true // Gioco ripreso con successo
            } else {
                _uiState.update { it.copy(isLoading = false) } // Non c'era un gioco da caricare
                return false // Nessun gioco da riprendere
            }
        } catch (e: Exception) {
            Log.e("GameViewModel", "Errore durante il resume della partita: ${e.message}", e)
            _uiState.update { it.copy(isLoading = false) } // Gestisci l'errore
            return false
        }
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
        startTimer() //Metto qui la partenza del timer, altrimenti parte prima del caricamento della griglia
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
        viewModelScope.launch {
            Log.d("DB is open: ", db.isOpen.toString())
            val currentState = _uiState.value
            val currentGrid = currentState.sudokuGrid
            val currentSolutionGrid = solutionGrid // Assicurati che solutionGrid sia aggiornato

            if (currentGrid != null && currentSolutionGrid != null) {
                // Per ora, li imposto a valori di placeholder.
                // Dovrai avere delle variabili nel tuo GameState o ViewModel per questi.
                val remainingErrors =_uiState.value.errors; // Esempio, dovresti tracciarli
                val remainingHints =_uiState.value.remainingHints;  // Esempio, dovresti tracciarli

                val gameToSave = SavedGame(
                    // id = 1, // Se vuoi sovrascrivere sempre la stessa partita (per una singola slot di salvataggio)
                    // Altrimenti lascia che sia autogenerato se vuoi più slot
                    id=1,
                    currentGridState = currentGrid,
                    solutionGridState = currentSolutionGrid,
                    remainingErrors = remainingErrors, // Sostituisci con i valori reali
                    remainingHints = remainingHints,   // Sostituisci con i valori reali
                    elapsedTimeInSeconds = currentState.secondsElapsed,
                )
                try {
                    savedGameRepository.updateGame(gameToSave)
                    Log.d("GameViewModel", "Partita salvata con successo con ID: ${gameToSave.id} (se autogenerato)") // Aggiungi un log di successo
                } catch (e: Exception) { // Cattura l'eccezione specifica o una più generica se necessario
                    // Logga il tipo di eccezione e il messaggio
                    Log.e("GameViewModel", "Errore durante il salvataggio della partita: ${e.javaClass.simpleName} - ${e.message}", e)
                    // Puoi anche stampare lo stack trace completo, utile per il debug approfondito
                }
                // o updateGame se stai aggiornando una partita esistente

                Log.d("GameViewModel", "Partita salvata con ID: ${gameToSave.id} (se autogenerato)")
                _shouldNavigateHome.value = true
            } else {
                Log.w("GameViewModel", "Impossibile salvare la partita: griglia corrente o soluzione mancante.")
                _shouldNavigateHome.value = true // Naviga comunque a casa anche se il salvataggio fallisce? Decidi tu.
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
            gameStatsRepository.updateGameStats(newStats) // Assumendo che updateGameStats faccia un UPSERT (INSERT or UPDATE)
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
}