package com.therxmv.dirolreader.di

import android.content.Context
import androidx.room.Room
import com.therxmv.dirolreader.data.repository.ChannelRepositoryImpl
import com.therxmv.dirolreader.data.repository.ClientRepositoryImpl
import com.therxmv.dirolreader.data.repository.MessageRepositoryImpl
import com.therxmv.dirolreader.data.source.locale.DirolDatabase
import com.therxmv.dirolreader.data.source.locale.channel.ChannelLocaleDataSource
import com.therxmv.dirolreader.data.source.locale.message.MessageLocaleDataSource
import com.therxmv.dirolreader.domain.repository.ChannelRepository
import com.therxmv.dirolreader.domain.repository.ClientRepository
import com.therxmv.dirolreader.domain.repository.MessageRepository
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
    fun provideChannelRepositoryImpl(channelLocaleDataSource: ChannelLocaleDataSource): ChannelRepository {
        return ChannelRepositoryImpl(channelLocaleDataSource)
    }

    @Provides
    @Singleton
    fun provideMessageRepositoryImpl(messageLocaleDataSource: MessageLocaleDataSource): MessageRepository {
        return MessageRepositoryImpl(messageLocaleDataSource)
    }

    @Provides
    @Singleton
    fun provideChannelLocaleDataSource(dirolDatabase: DirolDatabase): ChannelLocaleDataSource {
        return ChannelLocaleDataSource(dirolDatabase.dirolDao())
    }

    @Provides
    @Singleton
    fun provideMessageLocaleDataSource(dirolDatabase: DirolDatabase): MessageLocaleDataSource {
        return MessageLocaleDataSource(dirolDatabase.dirolDao())
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