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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addChannel(channelEntity: ChannelEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addChannels(channelEntity: List<ChannelEntity>)
}