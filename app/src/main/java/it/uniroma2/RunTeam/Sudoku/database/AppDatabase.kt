package it.uniroma2.RunTeam.Sudoku.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.uniroma2.RunTeam.Sudoku.database.dao.SavedGameDao
import it.uniroma2.RunTeam.Sudoku.database.converter.SudokuGridConverter
import it.uniroma2.RunTeam.Sudoku.database.dao.GameStatsDao
import it.uniroma2.RunTeam.Sudoku.database.entity.GameStats
import it.uniroma2.RunTeam.Sudoku.database.entity.SavedGame

@Database(
    entities = [
        SavedGame::class,
        GameStats::class
    ],
    version = 10,
    exportSchema = false
)
@TypeConverters(SudokuGridConverter::class) // Registra il TypeConverter
abstract class AppDatabase : RoomDatabase() {

    abstract fun savedGameDao(): SavedGameDao
    abstract fun gameStatsDao(): GameStatsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "sudoku_database" // Nome del file del database
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}