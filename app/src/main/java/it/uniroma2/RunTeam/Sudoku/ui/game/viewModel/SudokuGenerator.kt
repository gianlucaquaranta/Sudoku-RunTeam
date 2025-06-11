package it.uniroma2.RunTeam.Sudoku.ui.game.viewModel

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import it.uniroma2.RunTeam.Sudoku.model.Difficulty
import it.uniroma2.RunTeam.Sudoku.model.SudokuGrid

class SudokuGenerator(private val context: Context) {

    fun generateSudoku(
        difficulty: Difficulty,
        onSuccess: (Pair<SudokuGrid, SudokuGrid>) -> Unit,
        onError: (String) -> Unit
    ) {

        val request = object : JsonObjectRequest(
            Method.GET,
            "$URL?difficulty=$difficulty",
            null,
            { response ->
                try {
                    Log.d("SudokuGenerator", "API Response: $response") // 👈 DEBUG
                    val puzzleGrid = parseGrid(response.getJSONArray("puzzle"))
                    val solutionGrid = parseGrid(response.getJSONArray("solution"))

                    val puzzleSudoku = SudokuGrid.fromValues(puzzleGrid, difficulty)
                    val solutionSudoku = SudokuGrid.fromValues(solutionGrid, difficulty)
                    //Viene restituita una pair con puzzle e la sua soluzione
                    onSuccess(Pair(puzzleSudoku, solutionSudoku))
                } catch (e: Exception) {
                    onError("Parsing error: ${e.message}")
                }
            },
            { error ->
                onError("Volley error: ${error.message}")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("X-Api-Key" to API_KEY)
            }
        }

        Volley.newRequestQueue(context).add(request)
    }

    // Rende l'array trovato nel JSON in array di array
    private fun parseGrid(jsonArray: org.json.JSONArray): Array<IntArray> {
        val grid = Array(9) { IntArray(9) }
        for (i in 0 until jsonArray.length()) {
            val rowArray = jsonArray.getJSONArray(i)
            for (j in 0 until rowArray.length()) {
                grid[i][j] = if (rowArray.isNull(j)) 0 else rowArray.getInt(j)

            }
        }
        return grid
    }

    companion object{
        const val URL = "https://api.api-ninjas.com/v1/sudokugenerate"
        const val API_KEY = "0PjTFrnrzqCafQhaPY7nSJiUUErClJjNf2l6Nct0"
    }

}
