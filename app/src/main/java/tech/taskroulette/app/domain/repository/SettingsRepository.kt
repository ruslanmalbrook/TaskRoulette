package tech.taskroulette.app.domain.repository

import kotlinx.coroutines.flow.Flow
import tech.taskroulette.app.domain.model.ConfettiStyle
import tech.taskroulette.app.domain.model.SpinSoundTheme
import tech.taskroulette.app.domain.model.Settings

interface SettingsRepository {
    val settings: Flow<Settings>

    suspend fun setSoundEnabled(isEnabled: Boolean): Unit

    suspend fun setHapticsEnabled(isEnabled: Boolean): Unit

    suspend fun setSpinSoundTheme(theme: SpinSoundTheme): Unit

    suspend fun setConfettiStyle(style: ConfettiStyle): Unit

    suspend fun setActiveTaskSetId(taskSetId: String): Unit
}


