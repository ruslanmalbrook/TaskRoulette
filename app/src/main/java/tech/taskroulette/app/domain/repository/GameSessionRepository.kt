package tech.taskroulette.app.domain.repository

import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.domain.model.GameSession

interface GameSessionRepository {
    fun observeSessions(): Flow<List<GameSession>>

    suspend fun getSession(sessionId: String): GameSession?

    suspend fun insertSession(session: GameSession): Unit
}


