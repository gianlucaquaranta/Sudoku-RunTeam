package it.uniroma2.RunTeam.Sudoku.database.dto

data class CellDto(
    val row: Int,
    val col: Int,
    val isStartingCell: Boolean,
    val initialValue: Int?,
    val oldValue: Int?,
    val newValue: Int?,
    val value: Int,
    val isSelected: Boolean,
    val isIncorrect: Boolean,
    val isValid: Boolean,
    val notes: Set<Int>
)
