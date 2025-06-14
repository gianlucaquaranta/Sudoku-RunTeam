package it.uniroma2.RunTeam.Sudoku.ui.statistics.components

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable

fun DifficultyTabs(
    difficulties: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    TabRow(selectedTabIndex = selectedIndex) {
        difficulties.forEachIndexed { index, title ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onTabSelected(index) },
                text = { Text(title) }
            )
        }
    }
}