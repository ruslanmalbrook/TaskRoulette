package tech.taskroulette.app.domain.usecase.taskset

import javax.inject.Inject
import tech.taskroulette.app.domain.IdGenerator
import tech.taskroulette.app.domain.model.TaskSet
import tech.taskroulette.app.domain.repository.TaskSetRepository

class SaveTaskSetUseCase @Inject constructor(
    private val idGenerator: IdGenerator,
    private val taskSetRepository: TaskSetRepository,
) {
    suspend fun execute(
        existingTaskSetId: String?,
        name: String,
    ): TaskSet {
        val normalizedName = name.trim()
        require(normalizedName.isNotBlank()) { "Task set name must not be blank." }

        val id = existingTaskSetId ?: idGenerator.newId()

        val taskSet = TaskSet(
            id = id,
            name = normalizedName,
        )

        taskSetRepository.upsert(taskSet)
        return taskSet
    }
}

