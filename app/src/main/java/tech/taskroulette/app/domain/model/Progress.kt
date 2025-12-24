package tech.taskroulette.app.domain.model

data class Progress(
    val currentStreak: Int,
    val longestStreak: Int,
    val lastCompletionDateEpochMillis: Long?,
)

