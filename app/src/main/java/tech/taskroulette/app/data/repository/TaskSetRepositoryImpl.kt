package tech.taskroulette.app.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.taskroulette.app.data.local.db.dao.TaskSetDao
import tech.taskroulette.app.data.mapper.toDomain
import tech.taskroulette.app.data.mapper.toEntity
import tech.taskroulette.app.domain.model.TaskSet
import tech.taskroulette.app.domain.repository.TaskSetRepository

class TaskSetRepositoryImpl @Inject constructor(
    private val taskSetDao: TaskSetDao,
) : TaskSetRepository {
    override fun observeTaskSets(): Flow<List<TaskSet>> =
        taskSetDao.observeTaskSets().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getTaskSets(): List<TaskSet> = taskSetDao.getTaskSets().map { it.toDomain() }

    override suspend fun upsert(taskSet: TaskSet): Unit = taskSetDao.upsert(taskSet.toEntity())
}



