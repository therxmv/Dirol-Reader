package com.therxmv.dirolreader.di.data

import android.content.Context
import androidx.room.Room
import com.therxmv.dirolreader.data.source.locale.db.DirolDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataSourceModule {

    @Provides
    @Singleton
    fun provideDirolDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            DirolDatabase::class.java,
            "Dirol.db"
        ).build()

    @Provides
    @Singleton
    fun provideDirolDao(database: DirolDatabase) = database.dirolDao()

}