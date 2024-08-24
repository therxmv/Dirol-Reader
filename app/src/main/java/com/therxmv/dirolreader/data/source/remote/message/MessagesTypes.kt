package com.therxmv.dirolreader.data.source.remote.message

import android.util.Log
import com.therxmv.dirolreader.domain.models.ChannelData
import com.therxmv.dirolreader.domain.models.MediaModel
import com.therxmv.dirolreader.domain.models.MediaType
import com.therxmv.dirolreader.domain.models.MessageModel
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import java.math.RoundingMode

fun handleMessageType(
    channel: ChannelData,
    message: TdApi.Message,
): MessageModel {
    val defaultModel = MessageModel(
        id = message.id,
        channelData = channel,
        timestamp = message.date,
        text = "This message type is not yet supported",
        mediaList = null
    )

    return when (message.content) {
        is TdApi.MessageText -> {
            defaultModel.copy(
                text = (message.content as TdApi.MessageText).text.toMarkdown(),
            )
        }

        is TdApi.MessagePhoto -> {
            val photo = (message.content as TdApi.MessagePhoto).photo.sizes.last()

            defaultModel.copy(
                text = (message.content as TdApi.MessagePhoto).caption.toMarkdown(),
                mediaList = mutableListOf(
                    MediaModel(
                        photo.photo.id,
                        photo.height,
                        photo.width,
                        photo.photo.size.toMbString(),
                        MediaType.PHOTO
                    )
                )
            )
        }

        is TdApi.MessageVideo -> {
            val video = (message.content as TdApi.MessageVideo).video

            defaultModel.copy(
                text = (message.content as TdApi.MessageVideo).caption.toMarkdown(),
                mediaList = mutableListOf(
                    MediaModel(
                        video.video.id,
                        video.height,
                        video.width,
                        video.video.size.toMbString(),
                        MediaType.VIDEO
                    )
                )
            )
        }

        is TdApi.MessageAnimation -> {
            val anim = (message.content as TdApi.MessageAnimation).animation

            defaultModel.copy(
                text = (message.content as TdApi.MessageAnimation).caption.toMarkdown(),
                mediaList = mutableListOf(
                    MediaModel(
                        anim.animation.id,
                        anim.height,
                        anim.width,
                        anim.animation.size.toMbString(),
                        MediaType.VIDEO
                    )
                )
            )
        }

        is TdApi.MessageSticker -> {
            val sticker = (message.content as TdApi.MessageSticker).sticker
            // TODO 2 resolve if sticker isAnimated
            defaultModel.copy(
                text = "",
                mediaList = mutableListOf(
                    MediaModel(
                        sticker.sticker.id,
                        sticker.height,
                        sticker.width,
                        sticker.sticker.size.toMbString(),
                        MediaType.PHOTO
                    )
                )
            )
        }

        is TdApi.MessageVideoNote -> {
            val video = (message.content as TdApi.MessageVideoNote).videoNote

            defaultModel.copy(
                text = "",
                mediaList = mutableListOf(
                    MediaModel(
                        video.video.id,
                        video.length,
                        video.length,
                        video.video.size.toMbString(),
                        MediaType.VIDEO
                    )
                )
            )
        }

        is TdApi.MessageAudio -> {
            val audio = (message.content as TdApi.MessageAudio).audio

            defaultModel.copy(
                text = (message.content as TdApi.MessageAudio).caption.toMarkdown(),
                mediaList = mutableListOf(
                    MediaModel(
                        audio.audio.id,
                        100,
                        100,
                        audio.audio.size.toMbString(),
                        MediaType.VIDEO
                    )
                )
            )
        }

        is TdApi.MessageVoiceNote -> {
            val voice = (message.content as TdApi.MessageVoiceNote).voiceNote

            defaultModel.copy(
                text = (message.content as TdApi.MessageVoiceNote).caption.toMarkdown(),
                mediaList = mutableListOf(
                    MediaModel(
                        voice.voice.id,
                        100,
                        100,
                        voice.voice.size.toMbString(),
                        MediaType.VIDEO
                    )
                )
            )
        }

        is TdApi.MessageDocument -> {
            defaultModel.copy(
                text = "*FILE is not yet supported* ${(message.content as TdApi.MessageDocument).caption.toMarkdown()}",
            )
        }

        else -> {
            Log.d("rozmi", message.content.toString())
            defaultModel
        }
    }
}

private fun TdApi.FormattedText.toMarkdown(): String =
    Client.execute(TdApi.GetMarkdownText(this)).text

private fun Long.toMbString() = (this / 1048576F)
    .toBigDecimal()
    .setScale(1, RoundingMode.UP)
    .toString()