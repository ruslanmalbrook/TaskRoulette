package tech.taskroulette.app.domain.usecase.taskset

import javax.inject.Inject
import tech.taskroulette.app.domain.model.TaskSet
import tech.taskroulette.app.domain.repository.TaskSetRepository

class InitializePresetTaskSetsUseCase @Inject constructor(
    private val taskSetRepository: TaskSetRepository,
) {
    suspend fun execute(): Unit {
        val existingSets = taskSetRepository.getTaskSets()
        if (existingSets.isNotEmpty()) {
            return // Reason: presets already initialized or user has custom sets.
        }

        // Reason: Initialize default set if migration didn't create it.
        val defaultSet = TaskSet(
            id = TaskSet.DEFAULT_ID,
            name = "Default",
        )
        taskSetRepository.upsert(defaultSet)
    }
}

