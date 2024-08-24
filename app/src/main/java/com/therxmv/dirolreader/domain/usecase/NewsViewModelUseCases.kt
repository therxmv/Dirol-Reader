package com.therxmv.dirolreader.domain.usecase

import com.therxmv.dirolreader.domain.usecase.channel.UpdateChannelRatingUseCase
import com.therxmv.dirolreader.domain.usecase.message.DownloadMediaAndGetPathUseCase
import com.therxmv.dirolreader.domain.usecase.message.GetMessagePagingUseCase
import com.therxmv.dirolreader.domain.usecase.message.GetReadablePostTimeUseCase
import com.therxmv.dirolreader.domain.usecase.message.GetUnreadChannelsFlowUseCase
import com.therxmv.dirolreader.domain.usecase.message.MarkMessageAsReadUseCase
import com.therxmv.dirolreader.domain.usecase.user.GetCurrentUserUseCase
import javax.inject.Inject

data class NewsViewModelUseCases @Inject constructor(
    val getCurrentUser: GetCurrentUserUseCase,
    val getNewsPaging: GetMessagePagingUseCase,
    val updateChannelRating: UpdateChannelRatingUseCase,
    val downloadMediaAndGetPath: DownloadMediaAndGetPathUseCase,
    val markMessageAsRead: MarkMessageAsReadUseCase,
    val getReadablePostTime: GetReadablePostTimeUseCase,
    val getUnreadChannelsFlow: GetUnreadChannelsFlowUseCase,
)
