package tech.taskroulette.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.data.local.db.entity.TaskEntity

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY title COLLATE NOCASE ASC")
    fun observeTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks ORDER BY title COLLATE NOCASE ASC")
    suspend fun getTasks(): List<TaskEntity>

    @Upsert
    suspend fun upsert(task: TaskEntity): Unit

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun delete(taskId: String): Unit
}


