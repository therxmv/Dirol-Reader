package com.therxmv.dirolreader.data.source.locale

import androidx.room.*
import com.therxmv.dirolreader.data.source.locale.channel.models.ChannelEntity
import com.therxmv.dirolreader.data.source.locale.message.models.MessageEntity
import com.therxmv.dirolreader.domain.models.PostModel
import com.therxmv.dirolreader.utils.CHANNELS_TABLE
import com.therxmv.dirolreader.utils.MESSAGES_TABLE

@Dao
interface DirolDao {
    @Transaction
    @Query(
        "SELECT $MESSAGES_TABLE.id, $MESSAGES_TABLE.messageThreadId, $MESSAGES_TABLE.channelId, " +
                "$MESSAGES_TABLE.date, $MESSAGES_TABLE.text, $MESSAGES_TABLE.photoPath, " +
                "$MESSAGES_TABLE.isViewed, $MESSAGES_TABLE.isLast, $CHANNELS_TABLE.title, $CHANNELS_TABLE.avatarPath " +
                "FROM $MESSAGES_TABLE INNER JOIN $CHANNELS_TABLE ON $MESSAGES_TABLE.channelId = $CHANNELS_TABLE.id " +
                "ORDER BY $CHANNELS_TABLE.rating DESC LIMIT :limit OFFSET :offset"
    )
//    @Query("SELECT * FROM $MESSAGES_TABLE ORDER BY date DESC LIMIT :limit OFFSET :offset")
    fun getMessagesByPage(limit: Int, offset: Int): List<PostModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMessage(messageEntity: MessageEntity)

    @Update
    fun updateMessage(messageEntity: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addChannel(channelEntity: ChannelEntity): Long

    @Query("UPDATE $CHANNELS_TABLE SET title = :title, avatarPath = :avatarPath WHERE id = :id")
    fun updateChannel(id: Long, title: String, avatarPath: String?)

    @Transaction
    fun insertOrUpdateChannel(channelEntity: ChannelEntity) {
        val id = addChannel(channelEntity)
        if(id == -1L) updateChannel(channelEntity.id, channelEntity.title, channelEntity.avatarPath)
    }
}