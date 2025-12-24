package tech.taskroulette.app.domain.repository

import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.domain.model.TaskSet

interface TaskSetRepository {
    fun observeTaskSets(): Flow<List<TaskSet>>

    suspend fun getTaskSets(): List<TaskSet>

    suspend fun upsert(taskSet: TaskSet): Unit
}



