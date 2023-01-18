package com.flow.moviesearchflowhomework.di

import com.flow.moviesearchflowhomework.data.mapper.SearchItemMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {
    @Provides
    @Singleton
    fun provideSearchMapper(): SearchItemMapper = SearchItemMapper()
}