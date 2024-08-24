package com.therxmv.dirolreader.domain.repository

interface ChannelRepository {
    suspend fun updateChannelRating(id: Long, num: Int)
}