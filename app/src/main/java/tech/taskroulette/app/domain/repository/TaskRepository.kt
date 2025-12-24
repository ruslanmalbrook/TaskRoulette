package tech.taskroulette.app.domain.repository

import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.domain.model.Task

interface TaskRepository {
    fun observeTasks(taskSetId: String): Flow<List<Task>>

    suspend fun getTasks(taskSetId: String): List<Task>

    suspend fun upsert(task: Task): Unit

    suspend fun delete(taskId: String): Unit
}


