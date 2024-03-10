package com.therxmv.dirolreader.di.data

import com.therxmv.dirolreader.data.source.remote.channel.ChannelRemoteDataSource
import com.therxmv.dirolreader.data.source.remote.channel.ChannelRemoteSource
import com.therxmv.dirolreader.data.source.remote.message.MessageRemoteDataSource
import com.therxmv.dirolreader.data.source.remote.message.MessageSource
import com.therxmv.dirolreader.data.source.remote.user.UserRemoteDataSource
import com.therxmv.dirolreader.data.source.remote.user.UserSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsChannelRemoteDataSource(source: ChannelRemoteDataSource): ChannelRemoteSource

    @Binds
    @Singleton
    abstract fun bindsMessageRemoteDataSource(source: MessageRemoteDataSource): MessageSource

    @Binds
    @Singleton
    abstract fun bindsUserRemoteDataSource(source: UserRemoteDataSource): UserSource
}