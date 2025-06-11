package it.uniroma2.RunTeam.Sudoku.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.uniroma2.RunTeam.Sudoku.ui.home.HomeScreen
import it.uniroma2.RunTeam.Sudoku.ui.game.GameScreen
import it.uniroma2.RunTeam.Sudoku.ui.statistics.StatisticsScreen

@Composable
fun SudokuNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(onPlayClick = {
                navController.navigate(NavRoutes.GAME)
            }, onStatisticsClick = { navController.navigate(NavRoutes.STATISTICS) })
        }

        composable(NavRoutes.GAME) {
            GameScreen(navController = navController)
        }

        composable(NavRoutes.STATISTICS){
            StatisticsScreen(navController= navController)
        }

    }
}