package tech.taskroulette.app.domain.model

data class GameSession(
    val id: String,
    val createdAtEpochMillis: Long,
    val selectedSnapshotTaskId: String,
    val tasksSnapshot: List<SessionTaskSnapshot>,
)


