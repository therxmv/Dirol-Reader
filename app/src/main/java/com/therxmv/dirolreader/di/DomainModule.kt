package com.therxmv.dirolreader.di

import android.content.Context
import com.therxmv.dirolreader.domain.repository.ChannelRepository
import com.therxmv.dirolreader.domain.repository.ClientRepository
import com.therxmv.dirolreader.domain.repository.UserRepository
import com.therxmv.dirolreader.domain.usecase.AuthViewModelUseCases
import com.therxmv.dirolreader.domain.usecase.client.CreateClientUseCase
import com.therxmv.dirolreader.domain.usecase.client.GetClientUseCase
import com.therxmv.dirolreader.domain.usecase.user.GetCurrentUserAvatarUseCase
import com.therxmv.dirolreader.domain.usecase.user.GetCurrentUserUseCase
import com.therxmv.dirolreader.domain.usecase.GetTdLibParametersUseCase
import com.therxmv.dirolreader.domain.usecase.NewsViewModelUseCases
import com.therxmv.dirolreader.domain.usecase.channel.AddChannelToLocaleUseCase
import com.therxmv.dirolreader.domain.usecase.channel.GetLocaleChannelsUseCase
import com.therxmv.dirolreader.domain.usecase.channel.GetRemoteChannelsIdsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {
    @Provides
    fun provideGetTdLibParametersUseCase(@ApplicationContext context: Context): GetTdLibParametersUseCase {
        return GetTdLibParametersUseCase(context)
    }

    @Provides
    fun provideCreateClientUseCase(clientRepository: ClientRepository): CreateClientUseCase {
        return CreateClientUseCase(clientRepository)
    }

    @Provides
    fun provideGetClientUseCase(clientRepository: ClientRepository): GetClientUseCase {
        return GetClientUseCase(clientRepository)
    }

    @Provides
    fun provideGetCurrentUserUseCase(userRepository: UserRepository): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(userRepository)
    }

    @Provides
    fun provideGetCurrentUserAvatarUseCase(userRepository: UserRepository): GetCurrentUserAvatarUseCase {
        return GetCurrentUserAvatarUseCase(userRepository)
    }

    @Provides
    fun provideAddChannelToLocaleUseCase(channelRepository: ChannelRepository): AddChannelToLocaleUseCase {
        return AddChannelToLocaleUseCase(channelRepository)
    }

    @Provides
    fun provideGetLocaleChannelsUseCase(channelRepository: ChannelRepository): GetLocaleChannelsUseCase {
        return GetLocaleChannelsUseCase(channelRepository)
    }

    @Provides
    fun provideGetRemoteChannelsIdsUseCase(channelRepository: ChannelRepository): GetRemoteChannelsIdsUseCase {
        return GetRemoteChannelsIdsUseCase(channelRepository)
    }

    @Provides
    fun provideAuthViewModelUseCases(
        createClientUseCase: CreateClientUseCase,
        getTdLibParametersUseCase: GetTdLibParametersUseCase
    ) = AuthViewModelUseCases(
        createClientUseCase,
        getTdLibParametersUseCase
    )

    @Provides
    fun provideNewsViewModelUseCases(
        getClientUseCase: GetClientUseCase,
        getCurrentUserUseCase: GetCurrentUserUseCase,
        getCurrentUserAvatarUseCase: GetCurrentUserAvatarUseCase,
        addChannelToLocaleUseCase: AddChannelToLocaleUseCase,
        getLocaleChannelsUseCase: GetLocaleChannelsUseCase,
        getRemoteChannelsIdsUseCase: GetRemoteChannelsIdsUseCase,
    ) = NewsViewModelUseCases(
        getClientUseCase,
        getCurrentUserUseCase,
        getCurrentUserAvatarUseCase,
        addChannelToLocaleUseCase,
        getLocaleChannelsUseCase,
        getRemoteChannelsIdsUseCase,
    )
}