package tech.taskroulette.app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import tech.taskroulette.app.domain.IdGenerator
import tech.taskroulette.app.domain.UuidIdGenerator
import tech.taskroulette.app.domain.random.DefaultRandomProvider
import tech.taskroulette.app.domain.random.RandomProvider

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindIdGenerator(impl: UuidIdGenerator): IdGenerator

    @Binds
    @Singleton
    abstract fun bindRandomProvider(impl: DefaultRandomProvider): RandomProvider
}


