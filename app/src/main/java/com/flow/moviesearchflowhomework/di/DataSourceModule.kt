package com.flow.moviesearchflowhomework.di

import com.flow.moviesearchflowhomework.data.datasource.LocalRecentSearchDataSource
import com.flow.moviesearchflowhomework.data.datasource.LocalRecentSearchDataSourceImpl
import com.flow.moviesearchflowhomework.data.datasource.RemoteSearchDataSource
import com.flow.moviesearchflowhomework.data.datasource.RemoteSearchDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindRemoteSearchDataSource(remoteSearchDataSource: RemoteSearchDataSourceImpl): RemoteSearchDataSource

    @Binds
    @Singleton
    abstract fun bindLocalSearchDataSource(localRecentSearchDataSource: LocalRecentSearchDataSourceImpl): LocalRecentSearchDataSource
}