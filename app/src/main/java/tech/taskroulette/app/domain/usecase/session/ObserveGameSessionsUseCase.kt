package tech.taskroulette.app.domain.usecase.session

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.domain.model.GameSession
import tech.taskroulette.app.domain.repository.GameSessionRepository

class ObserveGameSessionsUseCase @Inject constructor(
    private val gameSessionRepository: GameSessionRepository,
) {
    operator fun invoke(): Flow<List<GameSession>> = gameSessionRepository.observeSessions()
}


