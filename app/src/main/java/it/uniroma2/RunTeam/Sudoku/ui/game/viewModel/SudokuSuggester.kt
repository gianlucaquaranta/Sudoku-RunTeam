package it.uniroma2.RunTeam.Sudoku.ui.game.viewModel

import android.util.Log
import it.uniroma2.RunTeam.Sudoku.model.Cell
import it.uniroma2.RunTeam.Sudoku.model.SudokuGrid

object SudokuSuggester {

    fun getHint(grid: SudokuGrid): Cell? {
        return findNakedSingle(grid)
            ?: findHiddenSingle(grid)
    }

    private fun findNakedSingle(grid: SudokuGrid): Cell? {
        for (row in 0..8) {
            for (col in 0..8) {
                grid.getCell(row, col)?.let { cell ->
                    if (cell.value == 0) {
                        val possibleValues = getPossibleValues(grid, row, col)
                        if (possibleValues.size == 1) {
                            Log.d("findNakedsingle", "cell: $cell, value: ${possibleValues.first()}")
                            return cell.copy().apply { updateValue(possibleValues.first()) }
                        }
                    }
                }
            }
        }
        return null
    }

    private fun findHiddenSingle(grid: SudokuGrid): Cell? {
        // Cerca Hidden Single in righe, colonne e sottogriglie
        for (i in 0..8) {
            // Controlla righe
            findHiddenSingleInUnit(grid, UnitType.ROW, i)?.let {
                Log.d("findHiddenSingle-ROW", "$it")
                return it
            }
            // Controlla colonne
            findHiddenSingleInUnit(grid, UnitType.COLUMN, i)?.let {
                Log.d("findHiddenSingle-COLUMN", "$it")
                return it
            }
            // Controlla sottogriglie 3x3
            if (i % 3 == 0) {
                findHiddenSingleInUnit(grid, UnitType.BOX, i)?.let {
                    Log.d("findHiddenSingle-BOX", "$it")
                    return it
                }
            }
        }
        return null
    }

    private fun findHiddenSingleInUnit(grid: SudokuGrid, unitType: UnitType, index: Int): Cell? {
        val missingNumbers = (1..9).toMutableList()
        val emptyCells = mutableListOf<Cell>()

        // Trova numeri mancanti e celle vuote nell'unità specificata (riga/colonna/box)
        when (unitType) {
            UnitType.ROW -> {
                for (col in 0..8) {
                    grid.getCell(index, col)?.let { cell ->
                        cell.value.takeIf { it != 0 }?.let { missingNumbers.remove(it) }
                        if (cell.value == 0) emptyCells.add(cell)
                    }
                }
            }
            UnitType.COLUMN -> {
                for (row in 0..8) {
                    grid.getCell(row, index)?.let { cell ->
                        cell.value.takeIf { it != 0 }?.let { missingNumbers.remove(it) }
                        if (cell.value == 0) emptyCells.add(cell)
                    }
                }
            }
            UnitType.BOX -> {
                val startRow = (index / 3) * 3
                val startCol = (index % 3) * 3
                for (row in startRow until startRow + 3) {
                    for (col in startCol until startCol + 3) {
                        grid.getCell(row, col)?.let { cell ->
                            cell.value.takeIf { it != 0 }?.let { missingNumbers.remove(it) }
                            if (cell.value == 0) emptyCells.add(cell)
                        }
                    }
                }
            }
        }

        // Per ogni numero mancante, controlla se c'è una sola cella possibile
        for (num in missingNumbers) {
            val possibleCells = emptyCells.filter { cell ->
                isValidPlacement(grid, cell.row, cell.col, num)
            }
            if (possibleCells.size == 1) {
                return possibleCells[0].copy().apply { updateValue(num) }
            }
        }
        return null
    }

    private fun getPossibleValues(grid: SudokuGrid, row: Int, col: Int): List<Int> {
        return (1..9).filter { num -> isValidPlacement(grid, row, col, num) }
    }

    private fun isValidPlacement(grid: SudokuGrid, row: Int, col: Int, num: Int): Boolean {
        // Controlla riga
        for (c in 0..8) {
            if (grid.getCell(row, c)?.value == num) return false
        }
        // Controlla colonna
        for (r in 0..8) {
            if (grid.getCell(r, col)?.value == num) return false
        }
        // Controlla box 3x3
        val boxStartRow = (row / 3) * 3
        val boxStartCol = (col / 3) * 3
        for (r in boxStartRow until boxStartRow + 3) {
            for (c in boxStartCol until boxStartCol + 3) {
                if (grid.getCell(r, c)?.value == num) return false
            }
        }
        return true
    }

    private enum class UnitType { ROW, COLUMN, BOX }
}