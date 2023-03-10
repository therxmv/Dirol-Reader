package com.therxmv.dirolreader.di

import android.content.Context
import com.therxmv.dirolreader.domain.repository.ChannelRepository
import com.therxmv.dirolreader.domain.repository.ClientRepository
import com.therxmv.dirolreader.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {
    @Provides
    fun provideGetClientUseCase(clientRepository: ClientRepository): GetClientUseCase {
        return GetClientUseCase(clientRepository)
    }

    @Provides
    fun provideGetTdLibParametersUseCase(@ApplicationContext context: Context): GetTdLibParametersUseCase {
        return GetTdLibParametersUseCase(context)
    }

    @Provides
    fun provideAddChannelToRoomUseCase(channelRepository: ChannelRepository): AddChannelToRoomUseCase {
        return AddChannelToRoomUseCase(channelRepository)
    }

    @Provides
    fun provideGetChannelsByPageUseCase(channelRepository: ChannelRepository): GetChannelsByPageUseCase {
        return GetChannelsByPageUseCase(channelRepository)
    }
}