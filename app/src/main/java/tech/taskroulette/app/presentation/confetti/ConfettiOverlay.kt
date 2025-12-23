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
import tech.taskroulette.app.domain.model.ConfettiStyle
import kotlin.random.Random

@Composable
fun ConfettiOverlay(
    isRunning: Boolean,
    style: ConfettiStyle,
    modifier: Modifier = Modifier,
) {
    if (!isRunning) return

    val progress = remember { Animatable(0f) }
    val config = remember(style) { style.toConfig() }
    val particles = remember(style) {
        List(config.particleCount) { index ->
            ConfettiParticle(
                x = Random.nextFloat(),
                size = config.minSize + Random.nextFloat() * (config.maxSize - config.minSize),
                speed = config.minSpeed + Random.nextFloat() * (config.maxSpeed - config.minSpeed),
                rotation = Random.nextFloat() * 360f,
                color = config.colors[index % config.colors.size],
                shape = config.shapes[index % config.shapes.size],
            )
        }
    }

    LaunchedEffect(isRunning, style) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = config.durationMs),
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

            when (p.shape) {
                ConfettiShape.Rect -> drawRect(
                    color = p.color.copy(alpha = alpha),
                    topLeft = Offset(px, py),
                    size = Size(p.size, p.size * 0.6f),
                )
                ConfettiShape.Circle -> drawCircle(
                    color = p.color.copy(alpha = alpha),
                    radius = p.size * 0.35f,
                    center = Offset(px, py),
                )
                ConfettiShape.Streamer -> drawRect(
                    color = p.color.copy(alpha = alpha),
                    topLeft = Offset(px, py),
                    size = Size(p.size * 0.5f, p.size * 1.6f),
                )
            }
        }
    }
}

private data class ConfettiParticle(
    val x: Float,
    val size: Float,
    val speed: Float,
    val rotation: Float,
    val color: Color,
    val shape: ConfettiShape,
)

private enum class ConfettiShape {
    Rect,
    Circle,
    Streamer,
}

private data class ConfettiConfig(
    val durationMs: Int,
    val particleCount: Int,
    val minSize: Float,
    val maxSize: Float,
    val minSpeed: Float,
    val maxSpeed: Float,
    val colors: List<Color>,
    val shapes: List<ConfettiShape>,
)

private fun ConfettiStyle.toConfig(): ConfettiConfig = when (this) {
    ConfettiStyle.Classic -> ConfettiConfig(
        durationMs = 1400,
        particleCount = 80,
        minSize = 6f,
        maxSize = 16f,
        minSpeed = 0.7f,
        maxSpeed = 2.1f,
        colors = DEFAULT_COLORS,
        shapes = listOf(ConfettiShape.Rect),
    )
    ConfettiStyle.Pop -> ConfettiConfig(
        durationMs = 1200,
        particleCount = 110,
        minSize = 5f,
        maxSize = 14f,
        minSpeed = 0.9f,
        maxSpeed = 2.4f,
        colors = POP_COLORS,
        shapes = listOf(ConfettiShape.Circle, ConfettiShape.Rect),
    )
    ConfettiStyle.Streamers -> ConfettiConfig(
        durationMs = 1600,
        particleCount = 70,
        minSize = 8f,
        maxSize = 18f,
        minSpeed = 0.6f,
        maxSpeed = 1.8f,
        colors = STREAMER_COLORS,
        shapes = listOf(ConfettiShape.Streamer),
    )
}

private val DEFAULT_COLORS: List<Color> = listOf(
    Color(0xFFE57373),
    Color(0xFF64B5F6),
    Color(0xFF81C784),
    Color(0xFFFFB74D),
    Color(0xFFBA68C8),
    Color(0xFF4DB6AC),
)

private val POP_COLORS: List<Color> = listOf(
    Color(0xFFFF5252),
    Color(0xFF448AFF),
    Color(0xFF69F0AE),
    Color(0xFFFFD740),
    Color(0xFFE040FB),
)

private val STREAMER_COLORS: List<Color> = listOf(
    Color(0xFFFFFFFF),
    Color(0xFFFFF59D),
    Color(0xFFB2EBF2),
    Color(0xFFFFCCBC),
)


