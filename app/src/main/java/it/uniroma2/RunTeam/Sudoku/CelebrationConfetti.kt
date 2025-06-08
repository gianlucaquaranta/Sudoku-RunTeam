package it.uniroma2.RunTeam.Sudoku

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.models.Size

import java.util.concurrent.TimeUnit

@Composable
fun CelebrationConfetti(show: Boolean) {
    if (!show) return

    val party = Party(
        emitter = Emitter(duration = 2, TimeUnit.SECONDS).perSecond(100),
        colors = listOf(0xFFFCE18A.toInt(), 0xFF726DFF.toInt(), 0xFFFA7DBA.toInt()),
        speed = 30f,
        maxSpeed = 50f,
        spread = 360,
        position = Position.Relative(0.5, 0.0),
        size = listOf(Size.MEDIUM, Size.LARGE),
        timeToLive = 3000L,
        fadeOutEnabled = true
    )

    KonfettiView(
        modifier = Modifier.fillMaxSize(),
        parties = listOf(party)
    )
}