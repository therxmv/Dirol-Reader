package com.therxmv.dirolreader.di

import android.content.Context
import androidx.room.Room
import com.therxmv.dirolreader.data.repository.ChannelRepositoryImpl
import com.therxmv.dirolreader.data.repository.ClientRepositoryImpl
import com.therxmv.dirolreader.data.repository.UserRepositoryImpl
import com.therxmv.dirolreader.data.source.locale.ChannelLocaleDataSource
import com.therxmv.dirolreader.data.source.locale.DirolDatabase
import com.therxmv.dirolreader.data.source.remote.ChannelRemoteDataSource
import com.therxmv.dirolreader.data.source.remote.UserRemoteDataSource
import com.therxmv.dirolreader.domain.repository.ChannelRepository
import com.therxmv.dirolreader.domain.repository.ClientRepository
import com.therxmv.dirolreader.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideClientRepositoryImpl(): ClientRepository {
        return ClientRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideUserRepositoryImpl(userRemoteDataSource: UserRemoteDataSource): UserRepository {
        return UserRepositoryImpl(userRemoteDataSource)
    }

    @Provides
    fun provideChannelRepositoryImpl(
        channelLocaleDataSource: ChannelLocaleDataSource,
        channelRemoteDataSource: ChannelRemoteDataSource
    ): ChannelRepository {
        return ChannelRepositoryImpl(channelLocaleDataSource, channelRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideChannelLocaleDataSource(dirolDatabase: DirolDatabase): ChannelLocaleDataSource {
        return ChannelLocaleDataSource(dirolDatabase.dirolDao())
    }

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(): UserRemoteDataSource {
        return UserRemoteDataSource()
    }

    @Provides
    fun provideChannelRemoteDataSource(): ChannelRemoteDataSource {
        return ChannelRemoteDataSource()
    }

    @Provides
    @Singleton
    fun provideDirolDatabase(@ApplicationContext context: Context): DirolDatabase {
        return Room.databaseBuilder(
            context,
            DirolDatabase::class.java,
            "Dirol.db"
        ).build()
    }
}