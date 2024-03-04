package com.therxmv.dirolreader.data.repository

import android.content.SharedPreferences
import com.therxmv.dirolreader.data.models.ChannelsRatingListModel
import com.therxmv.dirolreader.data.source.locale.AppSharedPrefsDataSource

class AppSharedPrefsRepository(
    private val appSharedPrefsDataSource: AppSharedPrefsDataSource
) {

    var isDynamic: Boolean
        get() = appSharedPrefsDataSource.isDynamic
        set(value) {
            appSharedPrefsDataSource.isDynamic = value
        }

    var isAutoDeleteEnabled: Boolean
        get() = appSharedPrefsDataSource.isAutoDeleteEnabled
        set(value) {
            appSharedPrefsDataSource.isAutoDeleteEnabled = value
        }

    var channelsRating: ChannelsRatingListModel
        get() = appSharedPrefsDataSource.channelsRating
        set(value) {
            appSharedPrefsDataSource.channelsRating = value
        }

    var isUpdateDownloaded: Boolean
        get() = appSharedPrefsDataSource.isUpdateDownloaded
        set(value) {
            appSharedPrefsDataSource.isUpdateDownloaded = value
        }

    fun isUpdateDownloadedChangeListener(callback: (isDownloaded: Boolean) -> Unit) =
        appSharedPrefsDataSource.isUpdateDownloadedChangeListener(callback)

    fun registerChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        appSharedPrefsDataSource.registerChangeListener(listener)
    }

    fun unregisterChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        appSharedPrefsDataSource.unregisterChangeListener(listener)
    }
}