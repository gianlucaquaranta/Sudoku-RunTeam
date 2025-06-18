package it.uniroma2.RunTeam.Sudoku.ui.statistics.components

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DifficultyTabs(
    difficulties: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier  // Aggiungi questo parametro
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier  // Applica il modifier qui
    ) {
        difficulties.forEachIndexed { index, title ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                text = { Text(text = title) }
            )
        }
    }
}