package it.uniroma2.RunTeam.Sudoku.ui.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun RulesFab(
    showRulesDialog: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (showRulesDialog) 360f else 0f,
        label = "rotation"
    )
    val scale by animateFloatAsState(
        targetValue = if (showRulesDialog) 1.2f else 1f,
        label = "scale"
    )

    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                rotationZ = rotation
            }
    ) {
        Icon(Icons.Default.Info, contentDescription = "Rules")
    }
}
