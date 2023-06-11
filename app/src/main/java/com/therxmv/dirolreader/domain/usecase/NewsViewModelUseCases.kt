package com.therxmv.dirolreader.domain.usecase

import com.therxmv.dirolreader.domain.usecase.channel.AddChannelToLocaleUseCase
import com.therxmv.dirolreader.domain.usecase.channel.GetLocaleChannelsUseCase
import com.therxmv.dirolreader.domain.usecase.channel.GetRemoteChannelsIdsUseCase
import com.therxmv.dirolreader.domain.usecase.client.GetClientUseCase
import com.therxmv.dirolreader.domain.usecase.message.GetMessagePagingUseCase
import com.therxmv.dirolreader.domain.usecase.user.GetCurrentUserAvatarUseCase
import com.therxmv.dirolreader.domain.usecase.user.GetCurrentUserUseCase

data class NewsViewModelUseCases(
    val getClientUseCase: GetClientUseCase,
    val getCurrentUserUseCase: GetCurrentUserUseCase,
    val getCurrentUserAvatarUseCase: GetCurrentUserAvatarUseCase,
    val addChannelToLocaleUseCase: AddChannelToLocaleUseCase,
    val getLocaleChannelsUseCase: GetLocaleChannelsUseCase,
    val getRemoteChannelsIdsUseCase: GetRemoteChannelsIdsUseCase,
    val getMessagePagingUseCase: GetMessagePagingUseCase,
)
