package com.therxmv.dirolreader.data.source.locale.channel.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.therxmv.dirolreader.utils.CHANNELS_TABLE

@Entity(tableName = CHANNELS_TABLE)
data class ChannelEntity (
    @PrimaryKey val id: Long,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("avatarPath") val avatarPath: String?,
    @ColumnInfo(name = "rating", defaultValue = "0") val rating: Int,
)