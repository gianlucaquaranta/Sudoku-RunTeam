package it.uniroma2.RunTeam.Sudoku.database.repository

import it.uniroma2.RunTeam.Sudoku.database.entity.SavedGame
import it.uniroma2.RunTeam.Sudoku.database.dao.SavedGameDao

class SavedGameRepository(private val savedGameDao: SavedGameDao) {

    suspend fun updateGame(savedGame: SavedGame) {
        savedGameDao.upsert(savedGame)
    }

    suspend fun getGameById(): List<SavedGame>? {
        return savedGameDao.getGameById()
    }

}