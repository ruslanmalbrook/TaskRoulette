package tech.taskroulette.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_sessions")
data class GameSessionEntity(
    @PrimaryKey
    val id: String,
    val createdAtEpochMillis: Long,
    val selectedSnapshotTaskId: String,
)


