package tech.taskroulette.app.domain.usecase

import javax.inject.Inject
import tech.taskroulette.app.domain.IdGenerator
import tech.taskroulette.app.domain.model.GameSession
import tech.taskroulette.app.domain.model.SessionTaskSnapshot
import tech.taskroulette.app.domain.model.Task

class CreateGameSessionUseCase @Inject constructor(
    private val idGenerator: IdGenerator,
) {
    fun createFromTasks(
        tasks: List<Task>,
        selectedTaskId: String,
        createdAtEpochMillis: Long,
    ): GameSession {
        require(tasks.isNotEmpty()) { "Cannot create session from empty task list." }

        val sessionId = idGenerator.newId()
        val snapshotTasks = tasks.mapIndexed { index, task ->
            SessionTaskSnapshot(
                id = idGenerator.newId(),
                originalTaskId = task.id,
                title = task.title,
                colorArgb = task.colorArgb,
                weight = task.weight.coerceAtLeast(1), // Reason: tolerate invalid persisted weight.
                orderIndex = index,
            )
        }

        val selectedSnapshot = snapshotTasks.firstOrNull { it.originalTaskId == selectedTaskId }
            ?: throw IllegalArgumentException("Selected task id is not present in task list.")

        return GameSession(
            id = sessionId,
            createdAtEpochMillis = createdAtEpochMillis,
            selectedSnapshotTaskId = selectedSnapshot.id,
            tasksSnapshot = snapshotTasks,
        )
    }
}


