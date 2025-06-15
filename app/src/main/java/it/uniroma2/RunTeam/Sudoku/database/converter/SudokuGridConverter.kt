package it.uniroma2.RunTeam.Sudoku.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import it.uniroma2.RunTeam.Sudoku.database.dto.SudokuGridDto
import it.uniroma2.RunTeam.Sudoku.model.Difficulty

class SudokuGridConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromSudokuGrid(gridDto: SudokuGridDto?): String? {
        return gridDto?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toSudokuGrid(json: String?): SudokuGridDto? {
        return json?.let {
            val type = object : TypeToken<SudokuGridDto>() {}.type
            gson.fromJson(it, type)
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