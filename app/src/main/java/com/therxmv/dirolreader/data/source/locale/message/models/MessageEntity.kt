package com.therxmv.dirolreader.data.source.locale.message.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.therxmv.dirolreader.utils.MESSAGES_TABLE

@Entity(tableName = MESSAGES_TABLE)
data class MessageEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo("messageThreadId") val messageThreadId: Long,
    @ColumnInfo("channelId") val channelId: Long,
    @ColumnInfo("date") val date: Int,
    @ColumnInfo("text") val text: String,
    @ColumnInfo("photoPath") val photoPath: String?,
    @ColumnInfo("isLast") val isLast: Boolean,
    @ColumnInfo("isNew") val isNew: Boolean,
)
