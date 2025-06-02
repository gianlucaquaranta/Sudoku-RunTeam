package it.uniroma2.RunTeam.Sudoku.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.uniroma2.RunTeam.Sudoku.ui.game.GameScreen
import it.uniroma2.RunTeam.Sudoku.ui.home.HomeScreen

@Composable
fun SudokuNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(onPlayClick = {
                navController.navigate(NavRoutes.GAME)
            })
        }

        composable(NavRoutes.GAME) {
            GameScreen()
        }
    }
}