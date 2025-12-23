package tech.taskroulette.app.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import tech.taskroulette.app.presentation.navigation.TaskRouletteNavHost
import tech.taskroulette.app.presentation.theme.TaskRouletteTheme

@Composable
fun TaskRouletteApp() {
    TaskRouletteTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            TaskRouletteNavHost(
                navController = rememberNavController(),
            )
        }
    }
}


