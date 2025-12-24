package tech.taskroulette.app.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.taskroulette.app.data.local.db.dao.TaskDao
import tech.taskroulette.app.data.mapper.toDomain
import tech.taskroulette.app.data.mapper.toEntity
import tech.taskroulette.app.domain.model.Task
import tech.taskroulette.app.domain.repository.TaskRepository

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
) : TaskRepository {

    override fun observeTasks(taskSetId: String): Flow<List<Task>> =
        taskDao.observeTasks(taskSetId).map { entities -> entities.map { it.toDomain() } }

    override suspend fun getTasks(taskSetId: String): List<Task> = taskDao.getTasks(taskSetId).map { it.toDomain() }

    override suspend fun upsert(task: Task): Unit = taskDao.upsert(task.toEntity())

    override suspend fun delete(taskId: String): Unit = taskDao.delete(taskId)
}


