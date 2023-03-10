package com.therxmv.dirolreader.di

import android.content.Context
import androidx.room.Room
import com.therxmv.dirolreader.data.repository.ChannelRepositoryImpl
import com.therxmv.dirolreader.data.repository.ClientRepositoryImpl
import com.therxmv.dirolreader.data.source.locale.ChannelDatabase
import com.therxmv.dirolreader.data.source.locale.ChannelLocaleDataSource
import com.therxmv.dirolreader.domain.repository.ChannelRepository
import com.therxmv.dirolreader.domain.repository.ClientRepository
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
    fun provideChannelLocaleDataSource(channelDatabase: ChannelDatabase): ChannelLocaleDataSource {
        return ChannelLocaleDataSource(channelDatabase.channelDao())
    }

    @Provides
    @Singleton
    fun provideChannelDatabase(@ApplicationContext context: Context): ChannelDatabase {
        return Room.databaseBuilder(
            context,
            ChannelDatabase::class.java,
            "Channels.db"
        ).build()
    }
}