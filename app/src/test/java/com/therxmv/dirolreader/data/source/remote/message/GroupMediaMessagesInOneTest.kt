package com.therxmv.dirolreader.data.source.remote.message

import com.therxmv.dirolreader.domain.models.ChannelData
import com.therxmv.dirolreader.domain.models.MediaModel
import com.therxmv.dirolreader.domain.models.MediaType
import com.therxmv.dirolreader.domain.models.MessageModel
import io.kotest.matchers.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class GroupMediaMessagesInOneTest(
    private val messagesList: List<MessageModel>,
    private val expectedSize: Int,
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(
            name = "{0}"
        )
        fun data() = listOf(
            arrayOf(case1, 2),
            arrayOf(case2, 3),
            arrayOf(case3, 2),
        )

        // case represents input list and expected size of output
        private val case1 = listOf(
            getMessageModel(
                id = 0,
                channelId = 0,
                mediaList = listOf(getMedia()),
            ),
            getMessageModel(
                id = 1,
                channelId = 0,
                mediaList = listOf(getMedia()),
            ),
            getMessageModel(
                id = 2,
                channelId = 0,
            ),
        )

        private val case2 = listOf(
            getMessageModel(
                id = 0,
                channelId = 0,
                mediaList = listOf(getMedia()),
            ),
            getMessageModel(
                id = 11,
                channelId = 0,
                mediaList = listOf(getMedia()),
            ),
            getMessageModel(
                id = 2,
                channelId = 0,
            ),
        )

        private val case3 = listOf(
            getMessageModel(
                id = 0,
                channelId = 0,
                mediaList = listOf(getMedia()),
            ),
            getMessageModel(
                id = 1,
                channelId = 1,
                mediaList = listOf(getMedia()),
            ),
        )

        private fun getMessageModel(id: Long = 0L, channelId: Long = 0L, mediaList: List<MediaModel>? = null) = MessageModel(
            id = id,
            channelData = getChannelData(channelId),
            timestamp = id.toInt(),
            text = "text",
            mediaList = mediaList,
        )

        private fun getMedia() = MediaModel(
            id = 0,
            height = 10,
            width = 10,
            sizeInMb = "10",
            type = MediaType.PHOTO,
        )

        private fun getChannelData(id: Long = 0L) = ChannelData(
            id = id,
            rating = 0,
            name = "name",
            avatarPath = null,
        )
    }

    @Test
    fun `should group messages with media correctly`() {
        val result = messagesList.groupMediaMessagesInOne()

        result.size shouldBe expectedSize
    }

    @Test
    fun `text for grouped messages with media should be set correctly`() {
        val expectedText = "dirol reader"
        val messages = listOf(
            getMessageModel(
                id = 0,
                channelId = 0,
                mediaList = listOf(getMedia()),
            ),
            getMessageModel(
                id = 1,
                channelId = 0,
                mediaList = listOf(getMedia()),
            ).copy(text = expectedText),
        )

        val result = messages.groupMediaMessagesInOne()

        result.size shouldBe 1
        result.firstOrNull()?.mediaList?.size shouldBe 2
        result.firstOrNull()?.text shouldBe expectedText
    }
}