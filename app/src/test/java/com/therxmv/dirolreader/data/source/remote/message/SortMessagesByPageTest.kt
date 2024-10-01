package com.therxmv.dirolreader.data.source.remote.message

import com.therxmv.dirolreader.data.entity.ChannelEntity
import io.kotest.matchers.shouldBe
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SortMessagesByPageTest(
    private val channelsList: List<ChannelEntity>,
    private val expectedMap: Map<Int, List<ChannelEntity>>,
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(
            name = "{0}"
        )
        fun data() = listOf(
            arrayOf(case1.first, case1.second),
            arrayOf(case2.first, case2.second),
            arrayOf(case3.first, case3.second),
            arrayOf(case4.first, case4.second),
            arrayOf(case5.first, case5.second),
            arrayOf(case6.first, case6.second),
            arrayOf(case7.first, case7.second),
        )

        private val channel1Unread = createChannel(unreadCount = 1)
        private val channel10Unread = createChannel(unreadCount = 10)
        private val channel25Unread = createChannel(unreadCount = 25)

        // case represents input list and expected output
        private val case1 = listOf(channel1Unread) to mapOf(
            0 to listOf(channel1Unread),
        )

        private val case2 = listOf(channel10Unread, channel1Unread) to mapOf(
            0 to listOf(channel10Unread),
            1 to listOf(channel1Unread),
        )

        private val case3 = listOf(channel1Unread, channel10Unread) to mapOf(
            0 to listOf(channel1Unread, channel10Unread.copy(unreadCount = 9)),
            1 to listOf(channel10Unread.copy(unreadCount = 1)),
        )

        private val case4 = listOf(channel25Unread) to mapOf(
            0 to listOf(channel25Unread.copy(unreadCount = 10)),
            1 to listOf(channel25Unread.copy(unreadCount = 10)),
            2 to listOf(channel25Unread.copy(unreadCount = 5)),
        )

        private val case5 = listOf(channel25Unread, channel1Unread, channel10Unread) to mapOf(
            0 to listOf(channel25Unread.copy(unreadCount = 10)),
            1 to listOf(channel25Unread.copy(unreadCount = 10)),
            2 to listOf(channel25Unread.copy(unreadCount = 5), channel1Unread, channel10Unread.copy(unreadCount = 4)),
            3 to listOf(channel10Unread.copy(unreadCount = 6)),
        )

        private val case6 = listOf(channel25Unread, channel25Unread) to mapOf(
            0 to listOf(channel25Unread.copy(unreadCount = 10)),
            1 to listOf(channel25Unread.copy(unreadCount = 10)),
            2 to listOf(channel25Unread.copy(unreadCount = 5), channel25Unread.copy(unreadCount = 5)),
            3 to listOf(channel25Unread.copy(unreadCount = 10)),
            4 to listOf(channel25Unread.copy(unreadCount = 10)),
        )

        private val case7 = listOf(createChannel(8), createChannel(2), createChannel(3)) to mapOf(
            0 to listOf(createChannel(8), createChannel(2)),
            1 to listOf(createChannel(3)),
        )

        private fun createChannel(unreadCount: Int = 1) = ChannelEntity(
            id = unreadCount.toLong(),
            unreadCount = unreadCount,
            lastReadMessageId = 0L,
            rating = 0,
        )
    }

    @Test
    fun `should sort channels for pagination correctly`() {
        val result = channelsList.sortMessagesByPage()

        result shouldBe expectedMap
    }
}