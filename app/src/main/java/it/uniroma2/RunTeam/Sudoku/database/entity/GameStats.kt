package it.uniroma2.RunTeam.Sudoku.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import it.uniroma2.RunTeam.Sudoku.model.Difficulty

@Entity(tableName = "game_stats")
data class GameStats(
    @PrimaryKey
    val difficulty: Difficulty,
    val bestTimeSeconds: Int = Int.MAX_VALUE,
    val wonGames: Int = 0,
    val lostGames: Int = 0,
    val totalTimePlayedSeconds: Int = 0,
)
