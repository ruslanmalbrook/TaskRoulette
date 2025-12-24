package tech.taskroulette.app.domain.usecase.taskimport

import javax.inject.Inject
import kotlinx.serialization.json.Json
import tech.taskroulette.app.domain.IdGenerator
import tech.taskroulette.app.domain.model.Task
import tech.taskroulette.app.domain.model.TaskExport
import tech.taskroulette.app.domain.repository.TaskRepository
import tech.taskroulette.app.domain.repository.TaskSetRepository
import tech.taskroulette.app.domain.usecase.taskimport.ImportResult
import tech.taskroulette.app.domain.usecase.taskset.SaveTaskSetUseCase

class ImportTasksUseCase @Inject constructor(
    private val idGenerator: IdGenerator,
    private val taskRepository: TaskRepository,
    private val taskSetRepository: TaskSetRepository,
    private val saveTaskSetUseCase: SaveTaskSetUseCase,
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun execute(
        jsonContent: String,
        targetTaskSetId: String?,
        createNewSet: Boolean,
    ): ImportResult {
        val export = try {
            json.decodeFromString<TaskExport>(jsonContent)
        } catch (e: Exception) {
            return ImportResult.Error("Invalid JSON format: ${e.message}")
        }

        val finalTaskSetId = if (createNewSet) {
            val newTaskSet = saveTaskSetUseCase.execute(
                existingTaskSetId = null,
                name = export.taskSetName,
            )
            newTaskSet.id
        } else {
            targetTaskSetId ?: return ImportResult.Error("Target task set ID is required when not creating new set")
        }

        val tasks = export.tasks.map { item ->
            Task(
                id = idGenerator.newId(),
                title = item.title,
                colorArgb = item.colorArgb,
                weight = item.weight.coerceAtLeast(1),
                taskSetId = finalTaskSetId,
            )
        }

        tasks.forEach { task ->
            taskRepository.upsert(task)
        }

        return ImportResult.Success(
            taskSetId = finalTaskSetId,
            importedCount = tasks.size,
        )
    }
}

