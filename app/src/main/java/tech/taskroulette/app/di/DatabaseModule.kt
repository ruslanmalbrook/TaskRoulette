package tech.taskroulette.app.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import tech.taskroulette.app.data.local.db.Migrations
import tech.taskroulette.app.data.local.db.TaskRouletteDatabase
import tech.taskroulette.app.data.local.db.dao.GameSessionDao
import tech.taskroulette.app.data.local.db.dao.TaskDao
import tech.taskroulette.app.data.local.db.dao.TaskSetDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): TaskRouletteDatabase =
        Room.databaseBuilder(
            context,
            TaskRouletteDatabase::class.java,
            "taskroulette.db",
        )
            .addMigrations(Migrations.MIGRATION_1_2)
            .build()

    @Provides
    fun provideTaskDao(
        database: TaskRouletteDatabase,
    ): TaskDao = database.taskDao()

    @Provides
    fun provideTaskSetDao(
        database: TaskRouletteDatabase,
    ): TaskSetDao = database.taskSetDao()

    @Provides
    fun provideGameSessionDao(
        database: TaskRouletteDatabase,
    ): GameSessionDao = database.gameSessionDao()
}


