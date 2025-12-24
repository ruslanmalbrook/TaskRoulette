package tech.taskroulette.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val colorArgb: Int,
    val weight: Int,
    val taskSetId: String,
)


