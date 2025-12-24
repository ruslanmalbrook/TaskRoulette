package tech.taskroulette.app.domain.usecase.progress

import javax.inject.Inject
import tech.taskroulette.app.domain.repository.ProgressRepository

class MarkTaskCompletedUseCase @Inject constructor(
    private val progressRepository: ProgressRepository,
) {
    suspend fun execute(): Unit {
        progressRepository.markTaskCompleted()
    }
}

