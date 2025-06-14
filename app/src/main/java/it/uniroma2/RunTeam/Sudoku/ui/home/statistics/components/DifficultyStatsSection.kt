package it.uniroma2.RunTeam.Sudoku.ui.home.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import  androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import it.uniroma2.RunTeam.Sudoku.R

@Composable
fun DifficultyStatsSection() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        StatCard(label = context.getString(R.string.games_played), value = "-")
        StatCard(label = context.getString(R.string.games_won), value = "-")
        StatCard(label = context.getString(R.string.best_time), value = "-")
        StatCard(label = context.getString(R.string.average_time), value = "-")
        StatCard(label = context.getString(R.string.total_time), value = "-")


    }
}

