package com.therxmv.dirolreader.data.source.remote.message

import com.therxmv.common.Paging.PAGE_SIZE
import com.therxmv.dirolreader.data.entity.ChannelEntity
import com.therxmv.dirolreader.domain.models.MessageModel

/**
 * Group media messages in one album.
 *
 * If the time of near messages are the same and media is not null
 * then combine media in one list and update caption if not empty.
 */
fun List<MessageModel>.groupMediaMessagesInOne(): List<MessageModel> { // TODO 2 doesn't group well with paging
    val list = this
    if (list.size <= 1) return list

    var id = 0
    val temp = mutableListOf(
        list[id]
    )

    for (i in 1 until list.size) {
        val prevItem = temp[id]
        val currentItem = list[i]

        val addCurrentItem = {
            temp.add(currentItem)
            id++
        }

        when {
            prevItem.mediaList == null || currentItem.mediaList == null -> addCurrentItem()

            currentItem.timestamp - prevItem.timestamp <= 10 && currentItem.channelData.id == prevItem.channelData.id -> {
                val text = currentItem.text.takeIf { it.isNotEmpty() } ?: prevItem.text

                temp[id] = prevItem.copy(
                    id = currentItem.id,
                    text = text,
                    mediaList = prevItem.mediaList + currentItem.mediaList,
                )
            }

            else -> addCurrentItem()
        }
    }

    return temp
}

/**
 * Sorts all channels with unread messages into pages with max e.g. 10 messages.
 *
 * If channel with unread count fits within limit it will be added to the current page
 * else it will be split into two parts, first on page 1 and second on page 2.
 *
 * Example of map:
 *
 * - 1 to listOf(ChannelEntity(id = 1, unreadCount = 2), ChannelEntity(id = 2, unreadCount = 8))
 * - 2 to listOf(ChannelEntity(id = 2, unreadCount = 3), ChannelEntity(id = 3, unreadCount = 7))
 * - 3 to listOf(ChannelEntity(id = 3, unreadCount = 1))
 */
fun List<ChannelEntity>.sortMessagesByPage(): Map<Int, List<ChannelEntity>> {
    val remainingChannels = this.toMutableList()
    val limit = PAGE_SIZE

    var currentPage = 0
    val pages: MutableMap<Int, MutableList<ChannelEntity>> = mutableMapOf()

    while (remainingChannels.isNotEmpty()) {
        val channel = remainingChannels.removeAt(0) // definitely exists

        // Initialize the list for the current page if it doesn't exist
        if (pages[currentPage] == null) {
            pages[currentPage] = mutableListOf()
        }

        val currentCount = pages[currentPage]?.sumOf { it.unreadCount } ?: 0

        val offset = channel.unreadCount + currentCount - limit

        if (offset > 0) {
            val countThatFit = channel.unreadCount - offset
            pages[currentPage]?.add(channel.copy(unreadCount = countThatFit))

            // rest will be resolved on the next iteration
            val restOfThisChannel = channel.copy(unreadCount = offset)
            remainingChannels.add(0, restOfThisChannel)
            currentPage++
        } else {
            pages[currentPage]?.add(channel)

            if (currentCount + channel.unreadCount >= limit) {
                currentPage++
            }
        }
    }

    return pages
}