package it.uniroma2.RunTeam.Sudoku.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.uniroma2.RunTeam.Sudoku.database.entity.GameStats
import it.uniroma2.RunTeam.Sudoku.model.Difficulty

@Dao
interface GameStatsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(gameStats: GameStats): Long // Ritorna l'id della riga inserita o -1 se fallisce a causa di IGNORE

    @Update
    suspend fun update(gameStats: GameStats)

    @Query("SELECT * FROM game_stats WHERE difficulty = :difficulty")
    suspend fun getStatsByDifficulty(difficulty: Difficulty): GameStats?
}
