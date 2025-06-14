package it.uniroma2.RunTeam.Sudoku.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import it.uniroma2.RunTeam.Sudoku.model.Difficulty

@Entity(tableName = "game_stats")
data class GameStats(
    @PrimaryKey
    val difficulty: Difficulty,
    val bestTimeSeconds: Long = Long.MAX_VALUE, // Inizializza con un valore alto
    val gamesPlayed: Int = 0,
    val totalTimePlayedSeconds: Long = 0,
    // La media la calcoleremo dinamicamente o al momento del salvataggio
) {
    // Calcola la media del tempo di gioco. Ritorna 0 se non ci sono partite giocate.
    val averageTimeSeconds: Float
        get() = if (gamesPlayed > 0) totalTimePlayedSeconds.toFloat() / gamesPlayed else 0f
}
