package tech.taskroulette.app.domain.repository

import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.domain.model.Task

interface TaskRepository {
    fun observeTasks(): Flow<List<Task>>

    suspend fun getTasks(): List<Task>

    suspend fun upsert(task: Task): Unit

    suspend fun delete(taskId: String): Unit
}


