package it.uniroma2.RunTeam.Sudoku.database.repository

import it.uniroma2.RunTeam.Sudoku.database.dao.GameStatsDao
import it.uniroma2.RunTeam.Sudoku.database.entity.GameStats
import it.uniroma2.RunTeam.Sudoku.model.Difficulty

class GameStatsRepository(private val gameStatsDao: GameStatsDao) {

    suspend fun getStatsByDifficulty(difficulty: Difficulty): GameStats? {
        return gameStatsDao.getStatsByDifficulty(difficulty)
    }

    suspend fun updateGameStats(gameStats : GameStats) {
        gameStatsDao.update(gameStats)
    }
}