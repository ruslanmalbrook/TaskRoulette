package tech.taskroulette.app.domain.usecase.progress

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.domain.model.Progress
import tech.taskroulette.app.domain.repository.ProgressRepository

class ObserveProgressUseCase @Inject constructor(
    private val progressRepository: ProgressRepository,
) {
    operator fun invoke(): Flow<Progress> = progressRepository.progress
}

