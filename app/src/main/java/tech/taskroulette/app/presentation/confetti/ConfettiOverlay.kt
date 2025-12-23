package tech.taskroulette.app.presentation.confetti

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

@Composable
fun ConfettiOverlay(
    isRunning: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!isRunning) return

    val progress = remember { Animatable(0f) }
    val particles = remember {
        List(PARTICLE_COUNT) { index ->
            ConfettiParticle(
                x = Random.nextFloat(),
                size = 6f + Random.nextFloat() * 10f,
                speed = 0.7f + Random.nextFloat() * 1.4f,
                rotation = Random.nextFloat() * 360f,
                color = DEFAULT_COLORS[index % DEFAULT_COLORS.size],
            )
        }
    }

    LaunchedEffect(isRunning) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = DURATION_MS),
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val t = progress.value
        val alpha = if (t < 0.85f) 1f else ((1f - t) / 0.15f).coerceIn(0f, 1f)

        particles.forEach { p ->
            val px = p.x * w
            val py = (t * h * p.speed) - (p.size * 2f)
            if (py > h + p.size) return@forEach

            drawRect(
                color = p.color.copy(alpha = alpha),
                topLeft = Offset(px, py),
                size = Size(p.size, p.size * 0.6f),
            )
        }
    }
}

private data class ConfettiParticle(
    val x: Float,
    val size: Float,
    val speed: Float,
    val rotation: Float,
    val color: Color,
)

private const val PARTICLE_COUNT: Int = 80
private const val DURATION_MS: Int = 1400

private val DEFAULT_COLORS: List<Color> = listOf(
    Color(0xFFE57373),
    Color(0xFF64B5F6),
    Color(0xFF81C784),
    Color(0xFFFFB74D),
    Color(0xFFBA68C8),
    Color(0xFF4DB6AC),
)


