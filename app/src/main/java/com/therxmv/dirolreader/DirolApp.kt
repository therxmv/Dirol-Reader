package com.therxmv.dirolreader

import android.app.Application
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DirolApp: Application() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
    }
}