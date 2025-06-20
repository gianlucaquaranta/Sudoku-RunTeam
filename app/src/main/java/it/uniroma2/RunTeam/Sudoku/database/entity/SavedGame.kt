package it.uniroma2.RunTeam.Sudoku.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import it.uniroma2.RunTeam.Sudoku.database.dto.SudokuGridDto

@Entity(tableName = "saved_games")
data class SavedGame(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val currentGridState: SudokuGridDto, // Stato attuale della griglia
    val solutionGridState: SudokuGridDto, // Griglia con la soluzione
    val remainingErrors: Int,
    val remainingHints: Int,
    val elapsedTimeInSeconds: Int,
)
