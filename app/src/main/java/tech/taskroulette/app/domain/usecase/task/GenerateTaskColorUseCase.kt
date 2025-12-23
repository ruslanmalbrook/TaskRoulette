package tech.taskroulette.app.domain.usecase.task

import javax.inject.Inject
import kotlin.math.abs

class GenerateTaskColorUseCase @Inject constructor() {

    /**
     * Generates a pleasant distinct color based on task index using golden-angle distribution.
     */
    fun generate(index: Int): Int {
        val safeIndex = abs(index)
        val hue = (safeIndex * GOLDEN_ANGLE_DEGREES) % 360.0
        val saturation = 0.62
        val lightness = 0.56
        return hslToArgb(
            h = hue,
            s = saturation,
            l = lightness,
        )
    }

    fun palette(size: Int): List<Int> {
        val count = size.coerceAtLeast(1)
        return (0 until count).map { idx -> generate(idx) }
    }

    private fun hslToArgb(
        h: Double,
        s: Double,
        l: Double,
    ): Int {
        val c = (1.0 - abs(2.0 * l - 1.0)) * s
        val hPrime = h / 60.0
        val x = c * (1.0 - abs(hPrime % 2.0 - 1.0))

        val (r1, g1, b1) = when {
            hPrime < 1.0 -> Triple(c, x, 0.0)
            hPrime < 2.0 -> Triple(x, c, 0.0)
            hPrime < 3.0 -> Triple(0.0, c, x)
            hPrime < 4.0 -> Triple(0.0, x, c)
            hPrime < 5.0 -> Triple(x, 0.0, c)
            else -> Triple(c, 0.0, x)
        }

        val m = l - c / 2.0
        val r = ((r1 + m) * 255.0).toInt().coerceIn(0, 255)
        val g = ((g1 + m) * 255.0).toInt().coerceIn(0, 255)
        val b = ((b1 + m) * 255.0).toInt().coerceIn(0, 255)

        return (0xFF shl 24) or (r shl 16) or (g shl 8) or b
    }

    companion object {
        private const val GOLDEN_ANGLE_DEGREES: Double = 137.50776405003785
    }
}


