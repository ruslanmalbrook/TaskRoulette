package tech.taskroulette.app.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import tech.taskroulette.app.data.local.db.dao.GameSessionDao
import tech.taskroulette.app.data.local.db.dao.TaskDao
import tech.taskroulette.app.data.local.db.entity.GameSessionEntity
import tech.taskroulette.app.data.local.db.entity.SessionTaskEntity
import tech.taskroulette.app.data.local.db.entity.TaskEntity

@Database(
    entities = [
        TaskEntity::class,
        GameSessionEntity::class,
        SessionTaskEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class TaskRouletteDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    abstract fun gameSessionDao(): GameSessionDao
}


