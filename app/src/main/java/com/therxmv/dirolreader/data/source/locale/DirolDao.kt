package com.therxmv.dirolreader.data.source.locale

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.therxmv.dirolreader.data.entity.ChannelEntity
import com.therxmv.dirolreader.utils.CHANNEL_TABLE

@Dao
interface DirolDao {
    @Query("SELECT * FROM $CHANNEL_TABLE ORDER BY $CHANNEL_TABLE.id DESC")
    fun getAllChannels(): List<ChannelEntity>

    @Query("UPDATE $CHANNEL_TABLE SET rating = rating + :num WHERE id = :id")
    fun updateChannelRating(id: Long, num: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addChannel(channelEntity: ChannelEntity): Long

    @Query("UPDATE $CHANNEL_TABLE SET unreadCount = :unreadCount, lastReadMessageId = :lastId WHERE id = :id")
    fun updateChannel(id: Long, unreadCount: Int, lastId: Long)

    @Transaction
    fun insertOrUpdateChannel(channelEntity: ChannelEntity) {
        val id = addChannel(channelEntity)
        if(id == -1L) updateChannel(channelEntity.id, channelEntity.unreadCount, channelEntity.lastReadMessageId)
    }
}