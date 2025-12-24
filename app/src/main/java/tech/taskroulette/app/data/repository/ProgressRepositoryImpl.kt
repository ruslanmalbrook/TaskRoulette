package tech.taskroulette.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import tech.taskroulette.app.domain.model.Progress
import tech.taskroulette.app.domain.repository.ProgressRepository

class ProgressRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : ProgressRepository {

    private object Keys {
        val currentStreak: Preferences.Key<Int> = intPreferencesKey("progress_current_streak")
        val longestStreak: Preferences.Key<Int> = intPreferencesKey("progress_longest_streak")
        val lastCompletionDate: Preferences.Key<Long> = longPreferencesKey("progress_last_completion_date")
    }

    override val progress: Flow<Progress> = dataStore.data.map { preferences ->
        Progress(
            currentStreak = preferences[Keys.currentStreak] ?: 0,
            longestStreak = preferences[Keys.longestStreak] ?: 0,
            lastCompletionDateEpochMillis = preferences[Keys.lastCompletionDate],
        )
    }

    override suspend fun markTaskCompleted(): Unit {
        val now = System.currentTimeMillis()
        dataStore.edit { preferences ->
            val lastCompletion = preferences[Keys.lastCompletionDate]
            val currentStreak = preferences[Keys.currentStreak] ?: 0
            val longestStreak = preferences[Keys.longestStreak] ?: 0

            val newStreak = if (lastCompletion != null && isSameDay(lastCompletion, now)) {
                // Reason: Already completed today, don't increment
                currentStreak
            } else if (lastCompletion != null && isConsecutiveDay(lastCompletion, now)) {
                // Reason: Consecutive day, increment streak
                currentStreak + 1
            } else {
                // Reason: New streak or gap, reset to 1
                1
            }

            preferences[Keys.currentStreak] = newStreak
            preferences[Keys.longestStreak] = maxOf(longestStreak, newStreak)
            preferences[Keys.lastCompletionDate] = now
        }
    }

    override suspend fun getProgress(): Progress {
        val preferences = dataStore.data.first()
        return Progress(
            currentStreak = preferences[Keys.currentStreak] ?: 0,
            longestStreak = preferences[Keys.longestStreak] ?: 0,
            lastCompletionDateEpochMillis = preferences[Keys.lastCompletionDate],
        )
    }

    private fun isSameDay(timestamp1: Long, timestamp2: Long): Boolean {
        val day1 = timestamp1 / (24 * 60 * 60 * 1000)
        val day2 = timestamp2 / (24 * 60 * 60 * 1000)
        return day1 == day2
    }

    private fun isConsecutiveDay(timestamp1: Long, timestamp2: Long): Boolean {
        val day1 = timestamp1 / (24 * 60 * 60 * 1000)
        val day2 = timestamp2 / (24 * 60 * 60 * 1000)
        return day2 == day1 + 1
    }
}

