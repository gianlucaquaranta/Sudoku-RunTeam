package it.uniroma2.RunTeam.Sudoku.ui.statistics.components

import android.app.Application
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import  androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.uniroma2.RunTeam.Sudoku.R
import it.uniroma2.RunTeam.Sudoku.ui.statistics.viewModel.StatisticsViewModel
import it.uniroma2.RunTeam.Sudoku.ui.statistics.viewModel.StatisticsViewModelFactory
import it.uniroma2.RunTeam.Sudoku.ui.statistics.components.CompactStatCard

@Composable
fun DifficultyStatsSection(
    index: Int,
    isPhoneLandscape: Boolean,
    // La difficoltà selezionata viene passata come parametro
) {

    val context = LocalContext.current
    val application =
        context.applicationContext as Application // Ottieni l'Application dal contesto

    // Fornisci la factory alla funzione viewModel()
    val statisticsViewModel: StatisticsViewModel = viewModel(
        factory = StatisticsViewModelFactory(application)
    )

    // Osserva lo StateFlow delle statistiche dal ViewModel.
    // Ogni volta che i dati in `statisticsViewModel.stats` cambiano,
    // questa variabile `stats` verrà aggiornata e la Composable si ricomporrà.
    val stats by statisticsViewModel.stats.collectAsState()

    LaunchedEffect(index) {
        statisticsViewModel.loadStats(index)
    }

    val content = @Composable {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(label = context.getString(R.string.games_played), value = stats.gamesPlayed)
            StatCard(label = context.getString(R.string.games_won), value = stats.gamesWon)
            StatCard(label = context.getString(R.string.best_time), value = stats.bestTime)
            StatCard(label = context.getString(R.string.average_time), value = stats.averageTime)
            StatCard(label = context.getString(R.string.total_time), value = stats.totalTime)
        }
    }

    if (isPhoneLandscape) {
        // Scroll solo in landscape
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            content()
        }
    } else {
        content()
    }
}





