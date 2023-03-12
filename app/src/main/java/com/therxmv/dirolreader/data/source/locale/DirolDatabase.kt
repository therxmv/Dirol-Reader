package com.therxmv.dirolreader.data.source.locale

import androidx.room.Database
import androidx.room.RoomDatabase
import com.therxmv.dirolreader.data.source.locale.channel.models.ChannelEntity
import com.therxmv.dirolreader.data.source.locale.message.models.MessageEntity

@Database(entities = [ChannelEntity::class, MessageEntity::class], version = 1, exportSchema = false)
abstract class DirolDatabase: RoomDatabase() {
    abstract fun dirolDao(): DirolDao
}