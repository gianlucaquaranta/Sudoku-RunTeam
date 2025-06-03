package it.uniroma2.RunTeam.Sudoku.ui.game.ViewModel

import it.uniroma2.RunTeam.Sudoku.model.Cell
import it.uniroma2.RunTeam.Sudoku.model.SudokuGrid

data class GameState(
    var isLoading: Boolean = true,
    var sudokuGrid: SudokuGrid ?= null,
    var selectedCell: Cell ?= null,
    var isNoteMode: Boolean = false,
    var secondsElapsed: Int = 0
)