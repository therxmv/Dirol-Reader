package com.therxmv.dirolreader.di.data

import com.therxmv.dirolreader.data.repository.ChannelRepositoryImpl
import com.therxmv.dirolreader.data.repository.MessageRepositoryImpl
import com.therxmv.dirolreader.data.repository.UserRepositoryImpl
import com.therxmv.dirolreader.domain.repository.ChannelRepository
import com.therxmv.dirolreader.domain.repository.MessageRepository
import com.therxmv.dirolreader.domain.repository.UserRepository
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
    abstract fun bindsChannelRepository(repository: ChannelRepositoryImpl): ChannelRepository

    @Binds
    @Singleton
    abstract fun bindsMessageRepository(repository: MessageRepositoryImpl): MessageRepository

    @Binds
    @Singleton
    abstract fun bindsUserRepository(repository: UserRepositoryImpl): UserRepository
}