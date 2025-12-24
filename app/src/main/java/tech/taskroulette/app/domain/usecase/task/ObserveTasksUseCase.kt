package tech.taskroulette.app.domain.usecase.task

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.domain.model.Task
import tech.taskroulette.app.domain.repository.TaskRepository

class ObserveTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
) {
    operator fun invoke(taskSetId: String): Flow<List<Task>> = taskRepository.observeTasks(taskSetId)
}


