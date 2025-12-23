package tech.taskroulette.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "session_tasks",
    foreignKeys = [
        ForeignKey(
            entity = GameSessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["sessionId"]),
        Index(value = ["sessionId", "orderIndex"], unique = true),
    ],
)
data class SessionTaskEntity(
    @PrimaryKey
    val id: String,
    val sessionId: String,
    val originalTaskId: String?,
    val title: String,
    val colorArgb: Int,
    val weight: Int,
    val orderIndex: Int,
)


