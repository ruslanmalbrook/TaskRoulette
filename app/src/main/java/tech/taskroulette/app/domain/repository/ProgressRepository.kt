package tech.taskroulette.app.domain.repository

import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.domain.model.Progress

interface ProgressRepository {
    val progress: Flow<Progress>

    suspend fun markTaskCompleted(): Unit

    suspend fun getProgress(): Progress
}

