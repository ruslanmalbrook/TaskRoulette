package tech.taskroulette.app.data.mapper

import tech.taskroulette.app.data.local.db.entity.GameSessionEntity
import tech.taskroulette.app.data.local.db.entity.SessionTaskEntity
import tech.taskroulette.app.data.local.db.model.GameSessionWithTasks
import tech.taskroulette.app.domain.model.GameSession
import tech.taskroulette.app.domain.model.SessionTaskSnapshot

fun GameSessionWithTasks.toDomain(): GameSession = GameSession(
    id = session.id,
    createdAtEpochMillis = session.createdAtEpochMillis,
    selectedSnapshotTaskId = session.selectedSnapshotTaskId,
    tasksSnapshot = tasks
        .sortedBy { it.orderIndex }
        .map { it.toDomain() },
)

fun GameSession.toEntity(): GameSessionEntity = GameSessionEntity(
    id = id,
    createdAtEpochMillis = createdAtEpochMillis,
    selectedSnapshotTaskId = selectedSnapshotTaskId,
)

fun SessionTaskEntity.toDomain(): SessionTaskSnapshot = SessionTaskSnapshot(
    id = id,
    originalTaskId = originalTaskId,
    title = title,
    colorArgb = colorArgb,
    weight = weight,
    orderIndex = orderIndex,
)

fun SessionTaskSnapshot.toEntity(sessionId: String): SessionTaskEntity = SessionTaskEntity(
    id = id,
    sessionId = sessionId,
    originalTaskId = originalTaskId,
    title = title,
    colorArgb = colorArgb,
    weight = weight,
    orderIndex = orderIndex,
)


