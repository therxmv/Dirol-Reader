package com.therxmv.dirolreader.data.source.locale.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChannelEntity (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "avatarPath") val avatarPath: Int?,
    @ColumnInfo(name = "channelName") val channelName: String,
    @ColumnInfo(name = "lastMessageText") val lastMessageText: String,
    @ColumnInfo(name = "lastMessageDate") val lastMessageDate: Int,
    @ColumnInfo(name = "photoPath") val photoPath: Int?,
    @ColumnInfo(name = "rating") val rating: Int,
)