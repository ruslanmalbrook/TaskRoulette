package tech.taskroulette.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import tech.taskroulette.app.presentation.history.HistoryScreen
import tech.taskroulette.app.presentation.home.HomeScreen
import tech.taskroulette.app.presentation.result.ResultScreen
import tech.taskroulette.app.presentation.settings.SettingsScreen
import tech.taskroulette.app.presentation.taskeditor.TaskEditorScreen

@Composable
fun TaskRouletteNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = TaskRouletteRoute.Home.PATTERN,
        modifier = modifier,
    ) {
        composable(
            route = TaskRouletteRoute.Home.PATTERN,
            arguments = listOf(
                navArgument(TaskRouletteRoute.Home.ARG_REPLAY_SESSION_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            ),
        ) { backStackEntry ->
            val replaySessionId = backStackEntry.arguments?.getString(TaskRouletteRoute.Home.ARG_REPLAY_SESSION_ID)
            HomeScreen(
                replaySessionId = replaySessionId,
                onEditTasksClick = { navController.navigate(TaskRouletteRoute.TaskEditor.route) },
                onHistoryClick = { navController.navigate(TaskRouletteRoute.History.route) },
                onSettingsClick = { navController.navigate(TaskRouletteRoute.Settings.route) },
                onNavigateToResult = { sessionId ->
                    navController.navigate(TaskRouletteRoute.Result.create(sessionId))
                },
            )
        }

        composable(TaskRouletteRoute.TaskEditor.route) {
            TaskEditorScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = TaskRouletteRoute.Result.PATTERN,
            arguments = listOf(
                navArgument(TaskRouletteRoute.Result.ARG_SESSION_ID) {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString(TaskRouletteRoute.Result.ARG_SESSION_ID)
            ResultScreen(
                sessionId = sessionId,
                onBack = { navController.popBackStack() },
                onBackToHome = {
                    navController.popBackStack(
                        route = TaskRouletteRoute.Home.PATTERN,
                        inclusive = false,
                    )
                },
                onHistoryClick = { navController.navigate(TaskRouletteRoute.History.route) },
            )
        }

        composable(TaskRouletteRoute.History.route) {
            HistoryScreen(
                onBack = { navController.popBackStack() },
                onNavigateToResult = { sessionId ->
                    navController.navigate(TaskRouletteRoute.Result.create(sessionId))
                },
                onNavigateToReplay = { sessionId ->
                    navController.navigate(TaskRouletteRoute.Home.create(replaySessionId = sessionId))
                },
            )
        }

        composable(TaskRouletteRoute.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
            )
        }
    }
}


