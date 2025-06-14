package it.uniroma2.RunTeam.Sudoku.ui.game.ViewModel

import it.uniroma2.RunTeam.Sudoku.model.Cell
import it.uniroma2.RunTeam.Sudoku.model.Difficulty
import it.uniroma2.RunTeam.Sudoku.model.SudokuGrid

data class GameState(
    var isLoading: Boolean = true,
    var sudokuGrid: SudokuGrid ?= null,
    var solutionGridState: SudokuGrid ?= null,
    var selectedCell: Cell ?= null,
    var errors: Int = 0,
    var remainingHints: Int = 0,
    var isNoteMode: Boolean = false,
    var secondsElapsed: Int = 0,
    val isGameCompleted: Boolean = false,
    val isGameLost: Boolean = false

)