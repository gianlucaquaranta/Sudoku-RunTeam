package it.uniroma2.RunTeam.Sudoku.model

data class SudokuGrid(
    val grid: Array<Array<Cell>>,
    val difficulty: Difficulty,
) {
    fun getCell(row: Int, col: Int): Cell? {
        return if (row in 0..8 && col in 0..8) grid[row][col] else null
    }

    companion object {

        fun fromValues(values: Array<IntArray>, difficulty: Difficulty): SudokuGrid {
            val grid = Array(9) { row ->
                Array(9) { col ->
                    val value = values[row][col]
                    Cell(
                        row = row,
                        col = col,
                        isStartingCell = value != 0,
                        initialValue = if (value != 0) value else 0,
                        oldValue = 0,
                        newValue = 0
                    )
                }
            }
            return SudokuGrid(grid = grid, difficulty = difficulty)
        }
    }

}
