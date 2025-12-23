package tech.taskroulette.app.domain.usecase.task

import javax.inject.Inject
import tech.taskroulette.app.domain.repository.TaskRepository

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
) {
    suspend operator fun invoke(taskId: String): Unit = taskRepository.delete(taskId)
}


