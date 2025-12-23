package tech.taskroulette.app.domain.repository

import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.domain.model.Settings

interface SettingsRepository {
    val settings: Flow<Settings>

    suspend fun setSoundEnabled(isEnabled: Boolean): Unit

    suspend fun setHapticsEnabled(isEnabled: Boolean): Unit
}


