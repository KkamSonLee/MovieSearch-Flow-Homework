package com.flow.moviesearchflowhomework.di

import android.content.Context
import androidx.room.Room
import com.flow.moviesearchflowhomework.data.local.DateTypeConverter
import com.flow.moviesearchflowhomework.data.local.SearchDAO
import com.flow.moviesearchflowhomework.data.local.SearchDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SearchDatabase {
        return Room.databaseBuilder(
            context,
            SearchDatabase::class.java,
            "search_database"
        ).addTypeConverter(DateTypeConverter()).build()
    }

    @Singleton
    @Provides
    fun provideTodayDao(database: SearchDatabase): SearchDAO = database.searchDAO()

}