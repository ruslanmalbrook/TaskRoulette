package tech.taskroulette.app.domain.usecase.session

import javax.inject.Inject
import tech.taskroulette.app.domain.model.GameSession
import tech.taskroulette.app.domain.repository.GameSessionRepository

class InsertGameSessionUseCase @Inject constructor(
    private val gameSessionRepository: GameSessionRepository,
) {
    suspend operator fun invoke(session: GameSession): Unit = gameSessionRepository.insertSession(session)
}


