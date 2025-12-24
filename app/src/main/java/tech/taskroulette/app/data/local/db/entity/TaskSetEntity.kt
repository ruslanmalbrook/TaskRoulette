package tech.taskroulette.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_sets")
data class TaskSetEntity(
    @PrimaryKey
    val id: String,
    val name: String,
)



