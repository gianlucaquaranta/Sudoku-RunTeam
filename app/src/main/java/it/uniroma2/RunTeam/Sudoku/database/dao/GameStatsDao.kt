package it.uniroma2.RunTeam.Sudoku.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.uniroma2.RunTeam.Sudoku.database.entity.GameStats
import it.uniroma2.RunTeam.Sudoku.model.Difficulty

@Dao
interface GameStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(gameStats: GameStats)

    @Query("SELECT * FROM game_stats WHERE difficulty = :difficulty")
    suspend fun getStatsByDifficulty(difficulty: Difficulty): GameStats?
}
