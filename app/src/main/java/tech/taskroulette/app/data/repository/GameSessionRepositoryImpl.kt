package tech.taskroulette.app.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.room.withTransaction
import tech.taskroulette.app.data.local.db.TaskRouletteDatabase
import tech.taskroulette.app.data.local.db.dao.GameSessionDao
import tech.taskroulette.app.data.mapper.toDomain
import tech.taskroulette.app.data.mapper.toEntity
import tech.taskroulette.app.domain.model.GameSession
import tech.taskroulette.app.domain.repository.GameSessionRepository

class GameSessionRepositoryImpl @Inject constructor(
    private val database: TaskRouletteDatabase,
    private val gameSessionDao: GameSessionDao,
) : GameSessionRepository {

    override fun observeSessions(): Flow<List<GameSession>> =
        gameSessionDao.observeSessionsWithTasks().map { sessions ->
            sessions.map { it.toDomain() }
        }

    override suspend fun getSession(sessionId: String): GameSession? =
        gameSessionDao.getSessionWithTasks(sessionId)?.toDomain()

    override suspend fun insertSession(session: GameSession): Unit {
        database.withTransaction {
            gameSessionDao.insertSession(session.toEntity())
            gameSessionDao.insertSessionTasks(
                session.tasksSnapshot.map { it.toEntity(sessionId = session.id) },
            )
        }
    }
}


