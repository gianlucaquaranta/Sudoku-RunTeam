package it.uniroma2.RunTeam.Sudoku.database.repository

import it.uniroma2.RunTeam.Sudoku.database.entity.SavedGame
import it.uniroma2.RunTeam.Sudoku.database.dao.SavedGameDao

class SavedGameRepository(private val savedGameDao: SavedGameDao) {

    suspend fun insertGame(savedGame: SavedGame) {
        savedGameDao.insertGame(savedGame)
    }

    suspend fun updateGame(savedGame: SavedGame) {
        savedGameDao.updateGame(savedGame)
    }

    suspend fun getGameById(): List<SavedGame>? {
        return savedGameDao.getGameById()
    }

    suspend fun deleteGameById(id: Int) {
        savedGameDao.deleteGameById(id)
    }
}