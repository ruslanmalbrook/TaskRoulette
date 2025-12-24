package tech.taskroulette.app.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Reason: Gradient utilities for modern, engaging visual design

@Composable
fun primaryGradient(): Brush {
    val colorScheme = MaterialTheme.colorScheme
    return Brush.linearGradient(
        colors = listOf(
            colorScheme.primary,
            colorScheme.tertiary,
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f),
    )
}

@Composable
fun secondaryGradient(): Brush {
    val colorScheme = MaterialTheme.colorScheme
    return Brush.linearGradient(
        colors = listOf(
            colorScheme.secondary,
            colorScheme.primaryContainer,
        ),
        start = Offset(0f, 0f),
        end = Offset(1000f, 1000f),
    )
}

@Composable
fun backgroundGradient(): Brush {
    val colorScheme = MaterialTheme.colorScheme
    val isDark = colorScheme.background == colorScheme.surfaceDim
    
    return if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                colorScheme.surface,
                colorScheme.surfaceContainerHigh,
            ),
            start = Offset(0f, 0f),
            end = Offset(1000f, 1000f),
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                colorScheme.surface,
                colorScheme.surfaceContainerLow,
            ),
            start = Offset(0f, 0f),
            end = Offset(1000f, 1000f),
        )
    }
}

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    gradient: Brush = backgroundGradient(),
    content: @Composable () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient),
    ) {
        content()
    }
}

