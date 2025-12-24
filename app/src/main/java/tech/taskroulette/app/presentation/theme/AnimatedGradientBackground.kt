package tech.taskroulette.app.presentation.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.cos
import kotlin.math.sin

// Reason: Ultra-soft pastel colors for gentle, eye-friendly gradient that never strains
private val pastelColors = listOf(
    Color(0xFFF5F9FC), // Very soft sky blue
    Color(0xFFF8F4FF), // Very soft lavender
    Color(0xFFFFF4F8), // Very soft pink
    Color(0xFFF4FFF4), // Very soft mint
    Color(0xFFFFF8F4), // Very soft peach
    Color(0xFFF4F8FF), // Very soft blue
    Color(0xFFFFFCF4), // Very soft cream
    Color(0xFFF8FFF4), // Very soft green
)

@Composable
fun AnimatedGradientBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gradient_animation")
    
    // Reason: Very slow animation (20 seconds per cycle) for extremely gentle color transitions
    val animationProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 20000, // 20 seconds for very slow, gentle transition
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "gradient_progress",
    )
    
    // Reason: Smoothly interpolate between colors for seamless flow
    val colorCount = pastelColors.size
    val baseIndex = (animationProgress * colorCount) % colorCount
    val nextIndex = ((baseIndex + 1) % colorCount).toInt()
    val currentIndex = baseIndex.toInt()
    val fraction = (animationProgress * colorCount) % 1f
    
    // Reason: Interpolate between current and next color for smooth transition
    val currentColor = pastelColors[currentIndex]
    val nextColor = pastelColors[nextIndex]
    
    val interpolatedColor1 = Color(
        red = currentColor.red + (nextColor.red - currentColor.red) * fraction,
        green = currentColor.green + (nextColor.green - currentColor.green) * fraction,
        blue = currentColor.blue + (nextColor.blue - currentColor.blue) * fraction,
        alpha = 1f,
    )
    
    // Reason: Create gradient with multiple interpolated colors for smooth flow
    val nextNextIndex = ((currentIndex + 2) % colorCount)
    val nextNextColor = pastelColors[nextNextIndex]
    val interpolatedColor2 = Color(
        red = nextColor.red + (nextNextColor.red - nextColor.red) * fraction,
        green = nextColor.green + (nextNextColor.green - nextColor.green) * fraction,
        blue = nextColor.blue + (nextNextColor.blue - nextColor.blue) * fraction,
        alpha = 1f,
    )
    
    // Reason: Animated gradient direction rotates slowly for flowing effect
    val angle = animationProgress * 360f
    val radius = 1200f
    val centerX = 600f
    val centerY = 600f
    val startX = centerX + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
    val startY = centerY + radius * sin(Math.toRadians(angle.toDouble())).toFloat()
    val endX = centerX - radius * cos(Math.toRadians(angle.toDouble())).toFloat()
    val endY = centerY - radius * sin(Math.toRadians(angle.toDouble())).toFloat()
    
    val gradient = Brush.linearGradient(
        colors = listOf(
            interpolatedColor1,
            interpolatedColor2,
            pastelColors[(currentIndex + 3) % colorCount],
        ),
        start = Offset(startX, startY),
        end = Offset(endX, endY),
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient),
    ) {
        content()
    }
}

