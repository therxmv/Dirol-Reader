package com.therxmv.dirolreader.data.source.locale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.therxmv.dirolreader.data.source.locale.model.ChannelEntity

@Dao
interface ChannelDao {
    @Query("SELECT * FROM channelEntity")
    fun getAllChannels(): List<ChannelEntity>

    @Query("SELECT * FROM channelEntity ORDER BY lastMessageDate DESC LIMIT :limit OFFSET :offset")
    fun getChannelsByPage(limit: Int, offset: Int): List<ChannelEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addChannel(channelEntity: ChannelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addChannel(channelEntity: List<ChannelEntity>)
}