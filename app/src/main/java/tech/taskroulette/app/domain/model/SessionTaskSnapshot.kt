package tech.taskroulette.app.domain.model

data class SessionTaskSnapshot(
    val id: String,
    val originalTaskId: String?,
    val title: String,
    val colorArgb: Int,
    val weight: Int,
    val orderIndex: Int,
)


