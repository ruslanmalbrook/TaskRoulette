package tech.taskroulette.app.domain.model

data class Task(
    val id: String,
    val title: String,
    val colorArgb: Int,
    val weight: Int,
    val taskSetId: String,
)


