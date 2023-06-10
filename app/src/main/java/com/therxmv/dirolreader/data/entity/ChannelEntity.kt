package com.therxmv.dirolreader.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.utils.CHANNEL_TABLE

@Entity(tableName = CHANNEL_TABLE)
data class ChannelEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "unreadCount") val unreadCount: Int,
    @ColumnInfo(name = "rating", defaultValue = "0") val rating: Int
)

fun ChannelEntity.toDomain() = ChannelModel(
    this.id,
    this.unreadCount,
    this.rating
)