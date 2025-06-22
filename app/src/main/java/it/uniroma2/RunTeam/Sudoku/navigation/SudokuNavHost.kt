package it.uniroma2.RunTeam.Sudoku.navigation

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import it.uniroma2.RunTeam.Sudoku.ui.home.HomeScreen
import it.uniroma2.RunTeam.Sudoku.ui.game.GameScreen
import it.uniroma2.RunTeam.Sudoku.ui.language.LanguageScreen
import it.uniroma2.RunTeam.Sudoku.ui.statistics.StatisticsScreen
import java.util.Locale
import it.uniroma2.RunTeam.Sudoku.LocalAppLocale
import it.uniroma2.RunTeam.Sudoku.updateLocale
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import it.uniroma2.RunTeam.Sudoku.ScreenWithNav
import it.uniroma2.RunTeam.Sudoku.model.AppTheme
import it.uniroma2.RunTeam.Sudoku.model.Difficulty
import it.uniroma2.RunTeam.Sudoku.ui.theme_option.SettingsScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SudokuNavHost(navController: NavHostController, onThemeChange: (AppTheme) -> Unit) {
    val context = LocalContext.current
    val currentLocale = remember {
        mutableStateOf(
            Locale(
                context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
                    .getString("language", Locale.getDefault().language) ?: "en"
            )
        )
    }

    val localizedContext = remember(currentLocale.value) {
        updateLocale(context, currentLocale.value)
    }

    CompositionLocalProvider(LocalAppLocale provides currentLocale) {
        CompositionLocalProvider(LocalContext provides localizedContext) {
            val currentRoute = navController.currentBackStackEntry?.destination?.route ?: NavRoutes.HOME

            AnimatedNavHost(
                navController = navController,
                startDestination = NavRoutes.HOME,
                enterTransition = { fadeIn() },
                exitTransition = { fadeOut() },
                popEnterTransition = { fadeIn() },
                popExitTransition = { fadeOut() }
            ) {
                composable(NavRoutes.HOME) {
                    ScreenWithNav(
                        currentRoute = NavRoutes.HOME,
                        onNavigate = { navController.navigate(it) }
                    ) { padding ->
                        HomeScreen(
                            onPlayClick = { difficulty ->
                                navController.navigate("game/${difficulty.name}")
                            },
                            onResumeClick = { navController.navigate(NavRoutes.RESUME) },
                            modifier = Modifier.padding(padding)
                        )

                    }
                }

                composable(NavRoutes.LANGUAGE) {
                    ScreenWithNav(
                        currentRoute = NavRoutes.LANGUAGE,
                        onNavigate = { navController.navigate(it) }
                    ) { padding ->
                        LanguageScreen(
                            navController = navController,
                            modifier = Modifier.padding(padding)
                        )
                    }
                }

                composable(NavRoutes.STATISTICS) {
                    ScreenWithNav(
                        currentRoute = NavRoutes.STATISTICS,
                        onNavigate = { navController.navigate(it) }
                    ) { padding ->
                        StatisticsScreen(
                            navController = navController,
                            modifier = Modifier.padding(padding)
                        )
                    }
                }

                composable("${NavRoutes.GAME}/{difficulty}") { backStackEntry ->
                    val difficultyArg = backStackEntry.arguments?.getString("difficulty") ?: "EASY"
                    val difficulty = Difficulty.valueOf(difficultyArg)
                    GameScreen(navController, NavRoutes.GAME, difficulty)
                }


                composable(NavRoutes.RESUME) {
                    GameScreen(navController, NavRoutes.RESUME, Difficulty.EASY)
                }

                composable(NavRoutes.SETTINGS) {
                    ScreenWithNav(
                        currentRoute = NavRoutes.SETTINGS,
                        onNavigate = { navController.navigate(it) }
                    ) { padding ->
                        SettingsScreen(
                            navController = navController,
                            modifier = Modifier.padding(padding),
                            onThemeSelected = onThemeChange
                        )
                    }
                }
            }
        }
    }
}
