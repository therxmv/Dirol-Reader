package com.therxmv.dirolreader.domain.usecase.message

import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import javax.inject.Inject

class MarkMessageAsReadUseCase @Inject constructor(
    private val client: Client,
) {

    operator fun invoke(messageId: Long, channelId: Long) {
        client.send(
            TdApi.ViewMessages(
                /* chatId = */ channelId,
                /* messageIds = */ longArrayOf(messageId),
                /* source = */ null,//MessageSourceChatHistory(),
                /* forceRead = */ true,
            )
        ) {}
    }
}