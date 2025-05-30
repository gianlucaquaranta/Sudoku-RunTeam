package it.uniroma2.RunTeam.Sudoku
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import kotlin.random.Random

class SudokuGame {
    private var _selectedCell = mutableStateOf<Pair<Int, Int>?>(null)
    private var _grid = mutableStateOf(Array(9) { IntArray(9) })
    private var _solution = Array(9) { IntArray(9) }
    private var _initialGrid = Array(9) { BooleanArray(9) }
    private var _hasWon = mutableStateOf(false)
    val hasWon: MutableState<Boolean> = _hasWon

    val selectedCell: State<Pair<Int, Int>?> = _selectedCell
    val grid: State<Array<IntArray>> = _grid

    init {
        generateNewPuzzle()
    }

    // Genera un nuovo Sudoku valido e random
    fun generateNewPuzzle() {
        // 1. Genera una soluzione completa
        _solution = generateFullGrid()

        // 2. Rimuovi alcuni numeri per creare il puzzle (difficoltà media: ~40 numeri rimossi)
        _grid.value = _solution.map { it.copyOf() }.toTypedArray()
        repeat(45) {
            val row = Random.nextInt(9)
            val col = Random.nextInt(9)
            _grid.value[row][col] = 0
            _initialGrid[row][col] = false
        }

        // 3. Marca le celle iniziali (non modificabili)
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                _initialGrid[i][j] = _grid.value[i][j] != 0
            }
        }
    }

    // Genera una griglia completa valida (backtracking)
    private fun generateFullGrid(): Array<IntArray> {
        val grid = Array(9) { IntArray(9) }
        fillGrid(grid)
        return grid
    }

    private fun fillGrid(grid: Array<IntArray>): Boolean {
        for (row in 0 until 9) {
            for (col in 0 until 9) {
                if (grid[row][col] == 0) {
                    val nums = (1..9).shuffled() // Randomizza l'ordine
                    for (num in nums) {
                        if (isValid(grid, row, col, num)) {
                            grid[row][col] = num
                            if (fillGrid(grid)) return true
                            grid[row][col] = 0 // Backtrack
                        }
                    }
                    return false
                }
            }
        }
        return true
    }

    // Controlla se un numero è valido in una cella
    private fun isValid(grid: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
        // Controlla riga e colonna
        for (i in 0 until 9) {
            if (grid[row][i] == num || grid[i][col] == num) return false
        }
        // Controlla il blocco 3x3
        val blockRow = (row / 3) * 3
        val blockCol = (col / 3) * 3
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (grid[blockRow + i][blockCol + j] == num) return false
            }
        }
        return true
    }

    // Inserisci numero solo se la cella è modificabile
    fun setNumber(number: Int) {
        val (row, col) = _selectedCell.value ?: return
        if (!_initialGrid[row][col]) {
            _grid.value[row][col] = number
            _grid.value = _grid.value.copyOf() // Force recomposition
            checkWin()
        }
    }

    // Seleziona cella (solo se vuota o modificabile)
    fun selectCell(row: Int, col: Int) {
        if (!_initialGrid[row][col] || _grid.value[row][col] == 0) {
            _selectedCell.value = Pair(row, col)
        }
    }

    // Controlla se il gioco è vinto
    private fun checkWin() {
        for (i in 0 until 9) {
            for (j in 0 until 9) {
                if (_grid.value[i][j] != _solution[i][j]) {
                    _hasWon.value = false
                    return
                }
            }
        }
        _hasWon.value = true
    }
}