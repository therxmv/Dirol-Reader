package com.therxmv.dirolreader.data.source.locale

import androidx.room.Database
import androidx.room.RoomDatabase
import com.therxmv.dirolreader.data.source.locale.model.ChannelEntity

@Database(entities = [ChannelEntity::class], version = 1, exportSchema = false)
abstract class ChannelDatabase: RoomDatabase() {
    abstract fun channelDao(): ChannelDao
}