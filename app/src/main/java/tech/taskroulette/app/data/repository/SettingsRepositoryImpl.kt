package tech.taskroulette.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.taskroulette.app.domain.model.Settings
import tech.taskroulette.app.domain.repository.SettingsRepository

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    private object Keys {
        val soundEnabled: Preferences.Key<Boolean> = booleanPreferencesKey("sound_enabled")
        val hapticsEnabled: Preferences.Key<Boolean> = booleanPreferencesKey("haptics_enabled")
    }

    override val settings: Flow<Settings> = dataStore.data.map { preferences ->
        Settings(
            isSoundEnabled = preferences[Keys.soundEnabled] ?: Settings.Default.isSoundEnabled,
            isHapticsEnabled = preferences[Keys.hapticsEnabled] ?: Settings.Default.isHapticsEnabled,
        )
    }

    override suspend fun setSoundEnabled(isEnabled: Boolean): Unit {
        dataStore.edit { preferences ->
            preferences[Keys.soundEnabled] = isEnabled
        }
    }

    override suspend fun setHapticsEnabled(isEnabled: Boolean): Unit {
        dataStore.edit { preferences ->
            preferences[Keys.hapticsEnabled] = isEnabled
        }
    }
}


