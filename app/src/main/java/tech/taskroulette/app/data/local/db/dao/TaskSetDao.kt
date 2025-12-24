package tech.taskroulette.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.data.local.db.entity.TaskSetEntity

@Dao
interface TaskSetDao {
    @Query("SELECT * FROM task_sets ORDER BY name COLLATE NOCASE ASC")
    fun observeTaskSets(): Flow<List<TaskSetEntity>>

    @Query("SELECT * FROM task_sets ORDER BY name COLLATE NOCASE ASC")
    suspend fun getTaskSets(): List<TaskSetEntity>

    @Upsert
    suspend fun upsert(taskSet: TaskSetEntity): Unit
}



