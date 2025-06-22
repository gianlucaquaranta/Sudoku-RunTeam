package it.uniroma2.RunTeam.Sudoku.ui.statistics.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.uniroma2.RunTeam.Sudoku.database.AppDatabase
import it.uniroma2.RunTeam.Sudoku.database.entity.GameStats
import it.uniroma2.RunTeam.Sudoku.database.repository.GameStatsRepository
import it.uniroma2.RunTeam.Sudoku.model.Difficulty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(application: Application) : ViewModel() {

    private val db = AppDatabase.getDatabase(application) // Corretto uso di application context
    private val gameStatsRepository = GameStatsRepository(db.gameStatsDao())

    private val _stats = MutableStateFlow(DifficultyStats())
    val stats: StateFlow<DifficultyStats> = _stats.asStateFlow()

    fun loadStats(index: Int) {
        viewModelScope.launch {
            try {
                val difficulty = when (index) {
                    0 -> Difficulty.EASY
                    1 -> Difficulty.MEDIUM
                    2 -> Difficulty.HARD
                    else -> throw IllegalArgumentException("Invalid index")
                }
                val fetchedStatsEntity: GameStats? =
                    gameStatsRepository.getStatsByDifficulty(difficulty)

                if (fetchedStatsEntity != null) {
                    val gamesPlayed = fetchedStatsEntity.wonGames + fetchedStatsEntity.lostGames
                    _stats.value = DifficultyStats(
                        gamesPlayed = gamesPlayed.toString(),
                        gamesWon = fetchedStatsEntity.wonGames.toString(),
                        bestTime = formatTime(fetchedStatsEntity.bestTimeSeconds),
                        averageTime = calculateAndFormatAverageTime(
                            fetchedStatsEntity.totalTimePlayedSeconds,
                            fetchedStatsEntity.wonGames
                        ),
                        totalTime = formatTime(fetchedStatsEntity.totalTimePlayedSeconds)
                    )
                } else {
                    // Nessuna statistica trovata per questa difficoltà, imposta valori di default o gestisci l'errore
                    _stats.value = DifficultyStats(
                        gamesPlayed = "0",
                        gamesWon = "0",
                        bestTime = "-",
                        averageTime = "-",
                        totalTime = "-"
                    )
                }
            } catch (e: Exception) {
                _stats.value = DifficultyStats() // Reimposta a valori di default in caso di errore
                Log.e("StatisticsViewModel", "Errore durante il caricamento delle statistiche", e)
            }
        }
    }

    private fun calculateAndFormatAverageTime(totalSeconds: Int?, gamesPlayed: Int?): String {
        if (totalSeconds == null || gamesPlayed == null || gamesPlayed <= 0 || totalSeconds <= 0) {
            return "-"
        }
        val averageTime = totalSeconds / gamesPlayed
        return formatTime(averageTime)
    }

    /**
     * Funzione di utilità di esempio per formattare il tempo da secondi a "mm:ss"
     */
    private fun formatTime(totalSeconds: Int?): String {
        if (totalSeconds == null || totalSeconds <= 0) return "-"
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
