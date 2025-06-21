package it.uniroma2.RunTeam.Sudoku.ui.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*

import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration

import it.uniroma2.RunTeam.Sudoku.model.Difficulty
import it.uniroma2.RunTeam.Sudoku.ui.home.components.DifficultySelectionDialog
import it.uniroma2.RunTeam.Sudoku.ui.home.components.PlayButton
import it.uniroma2.RunTeam.Sudoku.ui.home.components.ResumeButton
import it.uniroma2.RunTeam.Sudoku.ui.home.components.RulesDialog
import it.uniroma2.RunTeam.Sudoku.ui.home.components.RulesFab
import it.uniroma2.RunTeam.Sudoku.ui.home.components.TitleSection


@Composable
fun HomeScreen(
    onPlayClick: (Difficulty) -> Unit,
    onResumeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isPhoneLandscape = !isTablet && isLandscape

    var showRulesDialog by remember { mutableStateOf(false) }
    var showDifficultyDialog by remember { mutableStateOf(false) }
    val titleFontSize = when {
        isTablet -> 36.sp
        isPhoneLandscape -> 22.sp  // Ridotto ulteriormente
        else -> 32.sp
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 16.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (isPhoneLandscape) {
                Arrangement.Top
            } else {
                Arrangement.Center
            }
        ) {
            // Titolo
            TitleSection(
                isTablet = isTablet,
                fontSize = titleFontSize
            )

            Spacer(modifier = Modifier.height(if (isTablet) 48.dp else 50.dp))

            // Container per i pulsanti - modificato per landscape
            Column(
                modifier = if (isPhoneLandscape) {
                    Modifier
                        .fillMaxSize()
                } else {
                    Modifier.fillMaxWidth()
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                PlayButton(
                    onClick = { showDifficultyDialog = true },
                    modifier = Modifier
                        .widthIn(min = 260.dp, max = 350.dp)
                        .height(52.dp))

                Spacer(modifier = Modifier.height(5.dp))

                ResumeButton(
                    onClick = onResumeClick,
                    modifier = Modifier
                        .widthIn(min = 260.dp, max = 350.dp)
                        .height(52.dp)
                )
            }
        }

        // Floating Action Button per le regole - posizionato sopra la barra di navigazione
        RulesFab(
            showRulesDialog = showRulesDialog,
            onClick = { showRulesDialog = !showRulesDialog },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 20.dp,   // Regola la distanza dal bordo destro
                    bottom = 16.dp // Regola l'altezza dal fondo (senza calcoli automatici)
                )
        )

        // Dialoghi
        AnimatedVisibility(
            visible = showRulesDialog,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            RulesDialog(onDismiss = { showRulesDialog = false })
        }

        if (showDifficultyDialog) {
            DifficultySelectionDialog(
                onDismissRequest = { showDifficultyDialog = false },
                onDifficultySelected = { difficulty ->
                    showDifficultyDialog = false
                    onPlayClick(difficulty)
                }
            )
        }
    }
}

