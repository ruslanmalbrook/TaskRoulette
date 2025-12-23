package tech.taskroulette.app.domain.usecase.task

import javax.inject.Inject
import tech.taskroulette.app.domain.IdGenerator
import tech.taskroulette.app.domain.model.Task
import tech.taskroulette.app.domain.repository.TaskRepository

class SaveTaskUseCase @Inject constructor(
    private val idGenerator: IdGenerator,
    private val taskRepository: TaskRepository,
) {
    suspend fun execute(
        existingTaskId: String?,
        title: String,
        colorArgb: Int,
        weight: Int,
    ): Task {
        val normalizedTitle = title.trim()
        require(normalizedTitle.isNotBlank()) { "Task title must not be blank." }

        val normalizedWeight = weight.coerceAtLeast(1)
        val id = existingTaskId ?: idGenerator.newId()

        val task = Task(
            id = id,
            title = normalizedTitle,
            colorArgb = colorArgb,
            weight = normalizedWeight,
        )

        taskRepository.upsert(task)
        return task
    }
}


