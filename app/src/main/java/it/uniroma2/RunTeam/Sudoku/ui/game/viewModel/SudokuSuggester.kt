package it.uniroma2.RunTeam.Sudoku.ui.game.viewModel

import android.util.Log
import it.uniroma2.RunTeam.Sudoku.model.Cell
import it.uniroma2.RunTeam.Sudoku.model.SudokuGrid

object SudokuSuggester {

    fun getHint(grid: SudokuGrid, selected: Cell): Cell? {
        // 1. Prima controlla la cella selezionata
        if (selected.value == 0) {
            val possibleValues = getPossibleValues(grid, selected.row, selected.col)
            if(possibleValues.size==1){
                val newCell = selected.copy()
                newCell.updateValue(possibleValues.first())
                return newCell
            }
        }

        // 2. Cerca nella sotto-griglia 3x3
        findHintInSubgrid(grid, selected.row, selected.col)?.let {
            return it
        }

        // 3. Cerca nelle celle vicine (a strati concentrici)
        return findNearbyHint(grid, selected.row, selected.col, maxDistance = 8)
    }

    private fun findHintInSubgrid(grid: SudokuGrid, row: Int, col: Int): Cell? {
        val subgridStartRow = (row / 3) * 3
        val subgridStartCol = (col / 3) * 3

        // Cerca Naked Single nella sotto-griglia
        for (r in subgridStartRow until subgridStartRow + 3) {
            for (c in subgridStartCol until subgridStartCol + 3) {
                grid.getCell(r, c)?.let { cell ->
                    if (cell.value == 0 || cell.isIncorrect) {
                        val possibleValues = getPossibleValues(grid, r, c)
                        if (possibleValues.size == 1) {
                            val newCell = cell.copy()
                            newCell.updateValue(possibleValues.first())
                            return newCell
                        }
                    }
                }
            }
        }

        // Cerca Hidden Single nella sotto-griglia
        for (num in 1..9) {
            val possibleCells = mutableListOf<Cell>()
            for (r in subgridStartRow until subgridStartRow + 3) {
                for (c in subgridStartCol until subgridStartCol + 3) {
                    grid.getCell(r, c)?.let { cell ->
                        if ((cell.value == 0 || cell.isIncorrect) && isValidPlacement(grid, r, c, num)) {
                            possibleCells.add(cell)
                        }
                    }
                }
            }
            if (possibleCells.size == 1) {
                val newCell = possibleCells.first().copy()
                newCell.updateValue(num)
                return newCell
            }
        }

        return null
    }

    private fun findNearbyHint(grid: SudokuGrid, centerRow: Int, centerCol: Int, maxDistance: Int): Cell? {
        for (distance in 1..maxDistance) {
            val minRow = maxOf(0, centerRow - distance)
            val maxRow = minOf(8, centerRow + distance)
            val minCol = maxOf(0, centerCol - distance)
            val maxCol = minOf(8, centerCol + distance)
            Log.d("findNearbyHint", "minRow: $minRow; maxRow: $maxRow; minCol: $minCol; maxCol: $maxCol")
            val candidates = mutableListOf<Pair<Cell, Int>>()

            for (r in minRow..maxRow) {
                for (c in minCol..maxCol) {
                    // Salta la sotto-griglia già controllata
                    if ((r / 3) == (centerRow / 3) && (c / 3) == (centerCol / 3)) continue

                    grid.getCell(r, c)?.let { cell ->
                        if (cell.value == 0 || cell.isIncorrect) {
                            val possibleValues = getPossibleValues(grid, r, c)
                            if (possibleValues.isNotEmpty()) {
                                candidates.add(cell to possibleValues.size)
                            }
                        }
                    }
                }
            }

            // Prendi la cella con meno opzioni disponibili
            candidates.minByOrNull { it.second }?.let { (cell, _) ->
                getPossibleValues(grid, cell.row, cell.col).firstOrNull()?.let { value ->
                    val newCell = cell.copy()
                    newCell.updateValue(value)
                    return newCell
                }
            }
        }
        return null
    }

    private fun getPossibleValues(grid: SudokuGrid, row: Int, col: Int): Set<Int> {
        val possibleValues = (1..9).filter { num -> isValidPlacement(grid, row, col, num) }.toSet()
        val p = possibleValues.toString()
        Log.d("SudokuSuggester", "possible values: $p")
        return possibleValues
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
        val boxRow = (row / 3) * 3
        val boxCol = (col / 3) * 3
        for (r in boxRow until boxRow + 3) {
            for (c in boxCol until boxCol + 3) {
                if (grid.getCell(r, c)?.value == num) return false
            }
        }

        return true
    }
}