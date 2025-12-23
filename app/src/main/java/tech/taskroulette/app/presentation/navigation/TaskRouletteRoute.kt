package tech.taskroulette.app.presentation.navigation

sealed class TaskRouletteRoute(val route: String) {
    data object Home : TaskRouletteRoute("home") {
        const val ARG_REPLAY_SESSION_ID: String = "replaySessionId"
        const val PATTERN: String = "home?$ARG_REPLAY_SESSION_ID={$ARG_REPLAY_SESSION_ID}"

        fun create(replaySessionId: String?): String =
            if (replaySessionId.isNullOrBlank()) {
                route
            } else {
                "$route?$ARG_REPLAY_SESSION_ID=$replaySessionId"
            }
    }
    data object TaskEditor : TaskRouletteRoute("task_editor")
    data object Result : TaskRouletteRoute("result") {
        const val ARG_SESSION_ID: String = "sessionId"
        const val PATTERN: String = "result/{$ARG_SESSION_ID}"

        fun create(sessionId: String): String = "$route/$sessionId"
    }
    data object History : TaskRouletteRoute("history")
    data object Settings : TaskRouletteRoute("settings")
}


