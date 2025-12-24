package tech.taskroulette.app.data.local.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import tech.taskroulette.app.domain.model.TaskSet

object Migrations {
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase): Unit {
            // Task sets table.
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS task_sets (
                    id TEXT NOT NULL,
                    name TEXT NOT NULL,
                    PRIMARY KEY(id)
                )
                """.trimIndent(),
            )

            // Add taskSetId to existing tasks.
            db.execSQL(
                "ALTER TABLE tasks ADD COLUMN taskSetId TEXT NOT NULL DEFAULT '${TaskSet.DEFAULT_ID}'",
            )

            // Insert default set (id is stable for migration).
            db.execSQL(
                "INSERT OR IGNORE INTO task_sets (id, name) VALUES ('${TaskSet.DEFAULT_ID}', 'Default')",
            )
        }
    }
}



