package com.flow.moviesearchflowhomework.di

import com.flow.moviesearchflowhomework.data.repository.HomeRepositoryImpl
import com.flow.moviesearchflowhomework.domain.repository.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindHomRepository(homeRepositoryImpl: HomeRepositoryImpl): HomeRepository
}