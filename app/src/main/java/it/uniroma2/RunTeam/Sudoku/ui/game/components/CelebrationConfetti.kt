package it.uniroma2.RunTeam.Sudoku.ui.game.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit

@Composable
fun CelebrationConfetti(show: Boolean) {
    if (!show) return

    val emitter = Emitter(duration = 3, TimeUnit.SECONDS).perSecond(150)

    val colors = listOf(
        0xFFFCE18A.toInt(), 0xFFFF726D.toInt(), 0xFF17C3B2.toInt(),
        0xFF11A8AB.toInt(), 0xFF6A0572.toInt(), 0xFFE94F37.toInt()
    )

    val partyCenter = Party(
        emitter = emitter,
        speed = 20f,
        maxSpeed = 50f,
        damping = 0.9f,
        spread = 360,
        colors = colors,
        position = Position.Relative(0.5, 0.0),
        timeToLive = 4000L,
        fadeOutEnabled = true,
        shapes = listOf(Shape.Circle, Shape.Square),
        size = listOf(Size.SMALL, Size.MEDIUM, Size.LARGE)
    )

    val partyLeft = partyCenter.copy(position = Position.Relative(0.0, 0.0))
    val partyRight = partyCenter.copy(position = Position.Relative(1.0, 0.0))

    KonfettiView(
        modifier = Modifier.fillMaxSize(),
        parties = listOf(partyCenter, partyLeft, partyRight)
    )
}
