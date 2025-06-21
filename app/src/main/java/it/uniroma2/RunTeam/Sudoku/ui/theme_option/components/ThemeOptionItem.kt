package it.uniroma2.RunTeam.Sudoku.ui.theme_option.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nature
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ThemeOptionItem(
    title: String,
    onClick: (() -> Unit)? = null,
    flag: String,
    modifier: Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(enabled = onClick != null) { onClick?.invoke()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (flag) {
                    "scuro" -> Icons.Filled.DarkMode
                    "chiaro" -> Icons.Filled.LightMode
                    "jungle" -> Icons.Filled.Nature
                    "blue" -> Icons.Filled.Sailing
                    "aurora" -> Icons.Filled.Star
                    else -> Icons.Filled.LightMode // Un valore predefinito se nessuna condizione è soddisfatta
                },
                contentDescription = null
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}