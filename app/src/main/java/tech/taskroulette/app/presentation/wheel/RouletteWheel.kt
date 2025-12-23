package tech.taskroulette.app.presentation.wheel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import tech.taskroulette.app.domain.wheel.WheelSector

@Composable
fun RouletteWheel(
    sectors: List<WheelSector>,
    rotationDegrees: Float,
    highlightTaskId: String?,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val labelTextSizePx = with(density) { 12.sp.toPx() }
    val labelPaint = remember(labelTextSizePx) {
        android.graphics.Paint().apply {
            isAntiAlias = true
            textAlign = android.graphics.Paint.Align.CENTER
            textSize = labelTextSizePx
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        }
    }

    val pulse = rememberInfiniteTransition(label = "wheel_pulse").animateFloat(
        initialValue = 0.45f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 650),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse_alpha",
    )

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sizeMin = min(size.width, size.height)
            val diameter = sizeMin
            val topLeft = Offset(
                x = (size.width - diameter) / 2f,
                y = (size.height - diameter) / 2f,
            )
            val rect = Rect(topLeft, androidx.compose.ui.geometry.Size(diameter, diameter))

            sectors.forEach { sector ->
                val color = Color(sector.task.colorArgb)
                drawArc(
                    color = color,
                    startAngle = rotationDegrees + sector.startAngleDegrees.toFloat(),
                    sweepAngle = sector.sweepAngleDegrees.toFloat(),
                    useCenter = true,
                    topLeft = rect.topLeft,
                    size = rect.size,
                )
            }

            highlightTaskId?.let { taskId ->
                val sector = sectors.firstOrNull { it.task.id == taskId }
                if (sector != null) {
                    drawArc(
                        color = Color.White.copy(alpha = pulse.value),
                        startAngle = rotationDegrees + sector.startAngleDegrees.toFloat(),
                        sweepAngle = sector.sweepAngleDegrees.toFloat(),
                        useCenter = false,
                        style = Stroke(width = 6.dp.toPx()),
                        topLeft = rect.topLeft,
                        size = rect.size,
                    )
                }
            }

            // Labels (skip too-small sectors to avoid unreadable clutter)
            val nativeCanvas = drawContext.canvas.nativeCanvas
            val radius = diameter / 2f
            val center = Offset(topLeft.x + radius, topLeft.y + radius)
            val textRadius = radius * 0.62f

            sectors.forEach { sector ->
                if (sector.sweepAngleDegrees < 12.0) return@forEach

                val midAngle = rotationDegrees.toDouble() + sector.startAngleDegrees + sector.sweepAngleDegrees / 2.0
                val radians = Math.toRadians(midAngle)

                val x = center.x + (cos(radians) * textRadius).toFloat()
                val y = center.y + (sin(radians) * textRadius).toFloat()

                val bg = Color(sector.task.colorArgb)
                val textColor = if (bg.luminance() < 0.45f) Color.White else Color.Black
                labelPaint.color = textColor.toArgbInt()

                val sweepRadians = Math.toRadians(sector.sweepAngleDegrees)
                val availableArcPx = (sweepRadians * textRadius.toDouble() * 0.82).toFloat()

                labelPaint.textSize = when {
                    sector.sweepAngleDegrees >= 32.0 -> labelTextSizePx
                    sector.sweepAngleDegrees >= 22.0 -> (labelTextSizePx * 0.9f)
                    else -> (labelTextSizePx * 0.8f)
                }

                val title = truncateToWidth(
                    text = sector.task.title,
                    maxWidthPx = availableArcPx,
                    paint = labelPaint,
                )
                nativeCanvas.save()
                nativeCanvas.translate(x, y)
                nativeCanvas.rotate(midAngle.toFloat() + 90f)
                nativeCanvas.drawText(title, 0f, 0f, labelPaint)
                nativeCanvas.restore()
            }
        }

        Pointer(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun Pointer(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.padding(8.dp)) {
        val sizeMin = min(size.width, size.height)
        val diameter = sizeMin
        val topLeft = Offset(
            x = (size.width - diameter) / 2f,
            y = (size.height - diameter) / 2f,
        )
        val radius = diameter / 2f
        val centerX = topLeft.x + radius
        val topY = topLeft.y

        val pointerWidth = radius * 0.16f
        val pointerHeight = radius * 0.12f

        val path = Path().apply {
            moveTo(centerX, topY - 2f)
            lineTo(centerX - pointerWidth / 2f, topY + pointerHeight)
            lineTo(centerX + pointerWidth / 2f, topY + pointerHeight)
            close()
        }

        drawPath(
            path = path,
            color = Color.White,
        )
    }
}

private fun Color.toArgbInt(): Int = android.graphics.Color.argb(
    (alpha * 255).toInt(),
    (red * 255).toInt(),
    (green * 255).toInt(),
    (blue * 255).toInt(),
)

private fun truncateToWidth(
    text: String,
    maxWidthPx: Float,
    paint: android.graphics.Paint,
): String {
    val trimmed = text.trim()
    if (trimmed.isEmpty()) return ""
    if (maxWidthPx <= 0f) return ""

    if (paint.measureText(trimmed) <= maxWidthPx) return trimmed

    val ellipsis = "…"
    val ellipsisWidth = paint.measureText(ellipsis)
    if (ellipsisWidth >= maxWidthPx) return ""

    var end = trimmed.length
    while (end > 0) {
        val candidate = trimmed.substring(0, end) + ellipsis
        if (paint.measureText(candidate) <= maxWidthPx) return candidate
        end -= 1
    }
    return ellipsis
}


