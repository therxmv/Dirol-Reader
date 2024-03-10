package com.therxmv.dirolreader.domain.usecase

import com.therxmv.dirolreader.domain.usecase.channel.AddChannelToLocaleUseCase
import com.therxmv.dirolreader.domain.usecase.channel.GetRemoteChannelsIdsUseCase
import com.therxmv.dirolreader.domain.usecase.channel.UpdateChannelRatingUseCase
import com.therxmv.dirolreader.domain.usecase.client.GetClientUseCase
import com.therxmv.dirolreader.domain.usecase.message.GetMessageMediaUseCase
import com.therxmv.dirolreader.domain.usecase.message.GetMessagePagingUseCase
import com.therxmv.dirolreader.domain.usecase.user.GetCurrentUserAvatarUseCase
import com.therxmv.dirolreader.domain.usecase.user.GetCurrentUserUseCase
import javax.inject.Inject

data class NewsViewModelUseCases @Inject constructor(
    val getClientUseCase: GetClientUseCase,
    val getCurrentUserUseCase: GetCurrentUserUseCase,
    val getCurrentUserAvatarUseCase: GetCurrentUserAvatarUseCase,
    val addChannelToLocaleUseCase: AddChannelToLocaleUseCase,
    val getRemoteChannelsIdsUseCase: GetRemoteChannelsIdsUseCase,
    val getMessagePagingUseCase: GetMessagePagingUseCase,
    val updateChannelRatingUseCase: UpdateChannelRatingUseCase,
    val getMessageMediaUseCase: GetMessageMediaUseCase,
)
