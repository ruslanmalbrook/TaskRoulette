package tech.taskroulette.app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import tech.taskroulette.app.data.repository.GameSessionRepositoryImpl
import tech.taskroulette.app.data.repository.SettingsRepositoryImpl
import tech.taskroulette.app.data.repository.TaskRepositoryImpl
import tech.taskroulette.app.domain.repository.GameSessionRepository
import tech.taskroulette.app.domain.repository.SettingsRepository
import tech.taskroulette.app.domain.repository.TaskRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    abstract fun bindGameSessionRepository(impl: GameSessionRepositoryImpl): GameSessionRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}


