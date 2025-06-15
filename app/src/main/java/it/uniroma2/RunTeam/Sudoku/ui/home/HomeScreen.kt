package it.uniroma2.RunTeam.Sudoku.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import it.uniroma2.RunTeam.Sudoku.ui.home.components.PlayButton
import it.uniroma2.RunTeam.Sudoku.ui.home.components.ResumeButton
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import it.uniroma2.RunTeam.Sudoku.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import it.uniroma2.RunTeam.Sudoku.ui.home.components.DifficultySelectionDialog
import it.uniroma2.RunTeam.Sudoku.model.Difficulty
import it.uniroma2.RunTeam.Sudoku.navigation.NavRoutes

@Composable
fun HomeScreen(
    onPlayClick: (Difficulty) -> Unit, // Modificato per passare la difficoltà
    onResumeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showRulesDialog by remember { mutableStateOf(false) }
    var showDifficultyDialog by remember { mutableStateOf(false) } // Stato per il popup della difficoltà

    // Floating button animazioni
    val rotation by animateFloatAsState(
        targetValue = if (showRulesDialog) 360f else 0f,
        label = "rotation"
    )
    val scale by animateFloatAsState(
        targetValue = if (showRulesDialog) 1.2f else 1f,
        label = "scale"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "SUDOKU GAME",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(48.dp))

            PlayButton(
                onClick = { showDifficultyDialog = true }, // Mostra il popup della difficoltà
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ResumeButton(
                onClick = onResumeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )
        }

        FloatingActionButton(
            onClick = { showRulesDialog = !showRulesDialog },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    rotationZ = rotation
                }
        ) {
            Icon(Icons.Default.Info, contentDescription = "Rules")
        }

        if (showRulesDialog) {
            AnimatedVisibility(
                visible = showRulesDialog,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                AlertDialog(
                    onDismissRequest = { showRulesDialog = false },
                    confirmButton = {},
                    dismissButton = {},
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = context.getString(R.string.sudoku_title),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            IconButton(onClick = { showRulesDialog = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        }
                    },
                    text = {
                        Text(
                            text = context.getString(R.string.sudoku_rules),
                            modifier = Modifier.padding(16.dp)
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                )
            }
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