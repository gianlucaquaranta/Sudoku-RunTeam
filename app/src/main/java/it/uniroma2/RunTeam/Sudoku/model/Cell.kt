package it.uniroma2.RunTeam.Sudoku.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Cell(
    val row: Int,
    val col: Int,
    val isStartingCell: Boolean = false
) {
    var value by mutableStateOf(0)
    var isSelected by mutableStateOf(false)
    var isIncorrect by mutableStateOf(false)
    var notes by mutableStateOf(setOf<Int>())
}
