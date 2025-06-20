package it.uniroma2.RunTeam.Sudoku.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.uniroma2.RunTeam.Sudoku.database.entity.SavedGame

@Dao
interface SavedGameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Sostituisce la partita se esiste già un ID uguale
    suspend fun insertGame(savedGame: SavedGame)

    @Update
    suspend fun updateGame(savedGame: SavedGame)

    @Query("SELECT * FROM saved_games")
    suspend fun getGameById(): List<SavedGame>?

    @Query("DELETE FROM saved_games WHERE id = :id")
    suspend fun deleteGameById(id: Int)

}