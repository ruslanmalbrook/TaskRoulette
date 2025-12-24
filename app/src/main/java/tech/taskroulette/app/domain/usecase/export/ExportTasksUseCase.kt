package tech.taskroulette.app.domain.usecase.export

import javax.inject.Inject
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tech.taskroulette.app.domain.model.Task
import tech.taskroulette.app.domain.model.TaskExport
import tech.taskroulette.app.domain.model.TaskExportItem
import tech.taskroulette.app.domain.model.TaskSet
import tech.taskroulette.app.domain.repository.TaskRepository
import tech.taskroulette.app.domain.repository.TaskSetRepository

class ExportTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val taskSetRepository: TaskSetRepository,
) {
    suspend fun execute(taskSetId: String): String {
        val taskSet = taskSetRepository.getTaskSets()
            .firstOrNull { it.id == taskSetId }
            ?: throw IllegalArgumentException("Task set not found: $taskSetId")

        val tasks = taskRepository.getTasks(taskSetId)

        val export = TaskExport(
            taskSetName = taskSet.name,
            tasks = tasks.map { task ->
                TaskExportItem(
                    title = task.title,
                    colorArgb = task.colorArgb,
                    weight = task.weight,
                )
            },
        )

        return Json.encodeToString(export)
    }
}

