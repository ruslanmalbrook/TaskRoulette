package tech.taskroulette.app.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import tech.taskroulette.app.presentation.navigation.TaskRouletteNavHost
import tech.taskroulette.app.presentation.theme.AnimatedGradientBackground
import tech.taskroulette.app.presentation.theme.TaskRouletteTheme

@Composable
fun TaskRouletteApp() {
    TaskRouletteTheme {
        AnimatedGradientBackground(
            modifier = Modifier.fillMaxSize(),
        ) {
            TaskRouletteNavHost(
                navController = rememberNavController(),
            )
        }
    }
}


