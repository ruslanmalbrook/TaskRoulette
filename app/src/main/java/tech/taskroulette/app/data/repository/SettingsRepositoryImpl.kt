package tech.taskroulette.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.taskroulette.app.domain.model.ConfettiStyle
import tech.taskroulette.app.domain.model.SpinSoundTheme
import tech.taskroulette.app.domain.model.Settings
import tech.taskroulette.app.domain.model.TaskSet
import tech.taskroulette.app.domain.repository.SettingsRepository

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    private object Keys {
        val soundEnabled: Preferences.Key<Boolean> = booleanPreferencesKey("sound_enabled")
        val hapticsEnabled: Preferences.Key<Boolean> = booleanPreferencesKey("haptics_enabled")
        val spinSoundTheme: Preferences.Key<String> = stringPreferencesKey("spin_sound_theme")
        val confettiStyle: Preferences.Key<String> = stringPreferencesKey("confetti_style")
        val activeTaskSetId: Preferences.Key<String> = stringPreferencesKey("active_task_set_id")
    }

    override val settings: Flow<Settings> = dataStore.data.map { preferences ->
        Settings(
            isSoundEnabled = preferences[Keys.soundEnabled] ?: Settings.Default.isSoundEnabled,
            isHapticsEnabled = preferences[Keys.hapticsEnabled] ?: Settings.Default.isHapticsEnabled,
            spinSoundTheme = preferences[Keys.spinSoundTheme]
                ?.let { raw -> SpinSoundTheme.entries.firstOrNull { it.name == raw } }
                ?: Settings.Default.spinSoundTheme,
            confettiStyle = preferences[Keys.confettiStyle]
                ?.let { raw -> ConfettiStyle.entries.firstOrNull { it.name == raw } }
                ?: Settings.Default.confettiStyle,
            activeTaskSetId = preferences[Keys.activeTaskSetId] ?: TaskSet.DEFAULT_ID,
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

    override suspend fun setSpinSoundTheme(theme: SpinSoundTheme): Unit {
        dataStore.edit { preferences ->
            preferences[Keys.spinSoundTheme] = theme.name
        }
    }

    override suspend fun setConfettiStyle(style: ConfettiStyle): Unit {
        dataStore.edit { preferences ->
            preferences[Keys.confettiStyle] = style.name
        }
    }

    override suspend fun setActiveTaskSetId(taskSetId: String): Unit {
        dataStore.edit { preferences ->
            preferences[Keys.activeTaskSetId] = taskSetId
        }
    }
}


