package it.uniroma2.RunTeam.Sudoku.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import it.uniroma2.RunTeam.Sudoku.model.SudokuGrid // Assicurati che il percorso sia corretto

@Entity(tableName = "saved_games")
data class SavedGame(
    @PrimaryKey()
    val id: Int,
    val currentGridState: SudokuGrid, // Stato attuale della griglia
    val solutionGridState: SudokuGrid, // Griglia con la soluzione
    // Potresti aver bisogno di TypeConverters per SudokuGrid se Room non sa come gestirlo
    val remainingErrors: Int,
    val remainingHints: Int,
    val elapsedTimeInSeconds: Int,
)
