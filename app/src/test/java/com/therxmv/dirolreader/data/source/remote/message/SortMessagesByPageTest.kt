package com.therxmv.dirolreader.data.source.remote.message

import com.therxmv.dirolreader.data.entity.ChannelEntity
import io.kotest.matchers.shouldBe
import org.junit.Test

class SortMessagesByPageTest {

    private val channel1Unread = createChannel(unreadCount = 1)
    private val channel10Unread = createChannel(unreadCount = 10)
    private val channel25Unread = createChannel(unreadCount = 25)

    // case represents input list and expected output
    private val case1 = listOf(channel1Unread) to mapOf(
        0 to listOf(channel1Unread)
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

    private val cases = listOf(
        case1,
        case2,
        case3,
        case4,
        case5,
        case6,
    ).toMap()

    @Test
    fun `should sort channels for pagination correctly`() {
        cases.forEach {
            val result = sortMessagesByPage(it.key)

            result shouldBe it.value
        }
    }

    private fun createChannel(unreadCount: Int = 1) = ChannelEntity(
        id = unreadCount.toLong(),
        unreadCount = unreadCount,
        lastReadMessageId = 0L,
        rating = 0,
    )
}