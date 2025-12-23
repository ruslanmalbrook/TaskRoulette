package tech.taskroulette.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.data.local.db.entity.GameSessionEntity
import tech.taskroulette.app.data.local.db.entity.SessionTaskEntity
import tech.taskroulette.app.data.local.db.model.GameSessionWithTasks

@Dao
interface GameSessionDao {
    @Transaction
    @Query("SELECT * FROM game_sessions ORDER BY createdAtEpochMillis DESC")
    fun observeSessionsWithTasks(): Flow<List<GameSessionWithTasks>>

    @Transaction
    @Query("SELECT * FROM game_sessions WHERE id = :sessionId")
    suspend fun getSessionWithTasks(sessionId: String): GameSessionWithTasks?

    @Insert
    suspend fun insertSession(session: GameSessionEntity): Unit

    @Insert
    suspend fun insertSessionTasks(tasks: List<SessionTaskEntity>): Unit
}


