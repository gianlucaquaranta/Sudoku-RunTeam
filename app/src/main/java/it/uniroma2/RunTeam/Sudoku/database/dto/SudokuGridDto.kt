package it.uniroma2.RunTeam.Sudoku.database.dto

import it.uniroma2.RunTeam.Sudoku.model.Difficulty

data class SudokuGridDto(
    val grid: List<List<CellDto>>,
    val difficulty: Difficulty
)

