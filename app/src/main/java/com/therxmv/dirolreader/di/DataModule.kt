package com.therxmv.dirolreader.di

import android.content.Context
import androidx.room.Room
import com.therxmv.dirolreader.data.repository.AppSharedPrefsRepository
import com.therxmv.dirolreader.data.repository.ChannelRepositoryImpl
import com.therxmv.dirolreader.data.repository.ClientRepositoryImpl
import com.therxmv.dirolreader.data.repository.MessageRepositoryImpl
import com.therxmv.dirolreader.data.repository.UserRepositoryImpl
import com.therxmv.dirolreader.data.source.locale.AppSharedPrefsDataSource
import com.therxmv.dirolreader.data.source.locale.ChannelLocaleDataSource
import com.therxmv.dirolreader.data.source.locale.DirolDatabase
import com.therxmv.dirolreader.data.source.remote.ChannelRemoteDataSource
import com.therxmv.dirolreader.data.source.remote.MessageRemoteDataSource
import com.therxmv.dirolreader.data.source.remote.UserRemoteDataSource
import com.therxmv.dirolreader.domain.repository.ChannelRepository
import com.therxmv.dirolreader.domain.repository.ClientRepository
import com.therxmv.dirolreader.domain.repository.MessageRepository
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
    @Singleton
    fun provideChannelRepositoryImpl(
        channelLocaleDataSource: ChannelLocaleDataSource,
        channelRemoteDataSource: ChannelRemoteDataSource
    ): ChannelRepository {
        return ChannelRepositoryImpl(channelLocaleDataSource, channelRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideMessageRepositoryImpl(channelLocaleDataSource: ChannelLocaleDataSource, messageRemoteDataSource: MessageRemoteDataSource): MessageRepository {
        return MessageRepositoryImpl(channelLocaleDataSource, messageRemoteDataSource)
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
    @Singleton
    fun provideChannelRemoteDataSource(): ChannelRemoteDataSource {
        return ChannelRemoteDataSource()
    }

    @Provides
    @Singleton
    fun provideMessageRemoteDataSource(): MessageRemoteDataSource {
        return MessageRemoteDataSource()
    }

    @Provides
    @Singleton
    fun provideAppSharedPrefsDataSource(@ApplicationContext context: Context): AppSharedPrefsDataSource {
        return AppSharedPrefsDataSource(context)
    }

    @Provides
    @Singleton
    fun provideAppSharedPrefsRepository(appSharedPrefsDataSource: AppSharedPrefsDataSource): AppSharedPrefsRepository {
        return AppSharedPrefsRepository(appSharedPrefsDataSource)
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