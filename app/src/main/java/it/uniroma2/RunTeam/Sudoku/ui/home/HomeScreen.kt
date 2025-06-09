package it.uniroma2.RunTeam.Sudoku.ui.home

import it.uniroma2.RunTeam.Sudoku.ui.home.components.PlayButton
import it.uniroma2.RunTeam.Sudoku.ui.home.components.ResumeButton
import it.uniroma2.RunTeam.Sudoku.ui.home.components.StatisticsButton
import it.uniroma2.RunTeam.Sudoku.ui.home.components.SettingsButton
import it.uniroma2.RunTeam.Sudoku.ui.home.components.RulesButton
import it.uniroma2.RunTeam.Sudoku.ui.home.components.LanguageButton
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight



@Composable
fun HomeScreen(onPlayClick: () -> Unit,onStatisticsClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "SUDOKU GAME",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(48.dp))

        PlayButton(onClick = onPlayClick)
        Spacer(modifier = Modifier.height(12.dp))

        ResumeButton(onClick = {})
        Spacer(modifier = Modifier.height(12.dp))

        StatisticsButton(onClick = onStatisticsClick)
        Spacer(modifier = Modifier.height(12.dp))

        SettingsButton(onClick = {})
        Spacer(modifier = Modifier.height(12.dp))

        RulesButton(onClick = {})
        Spacer(modifier = Modifier.height(12.dp))

        LanguageButton(onClick = {})
    }
}
