package it.uniroma2.RunTeam.Sudoku.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.uniroma2.RunTeam.Sudoku.model.Difficulty
import it.uniroma2.RunTeam.Sudoku.model.SudokuGrid

class SudokuGridConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromSudokuGrid(grid: SudokuGrid
    ?): String? {
        return grid?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toSudokuGrid(json: String?): SudokuGrid? {
        return json?.let {
            val type = object : TypeToken<SudokuGrid>() {}.type
            gson.fromJson<SudokuGrid>(it, type)
        }
    }

    @TypeConverter
    fun fromDifficulty(value: Difficulty): String {
        return value.name
    }

    @TypeConverter
    fun toDifficulty(value: String): Difficulty {
        return Difficulty.valueOf(value)
    }
}