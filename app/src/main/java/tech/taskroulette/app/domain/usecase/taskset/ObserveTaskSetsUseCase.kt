package tech.taskroulette.app.domain.usecase.taskset

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.domain.model.TaskSet
import tech.taskroulette.app.domain.repository.TaskSetRepository

class ObserveTaskSetsUseCase @Inject constructor(
    private val taskSetRepository: TaskSetRepository,
) {
    operator fun invoke(): Flow<List<TaskSet>> = taskSetRepository.observeTaskSets()
}

