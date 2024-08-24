package com.therxmv.sharedpreferences.repository

import android.content.SharedPreferences
import com.therxmv.sharedpreferences.source.AppSharedPrefsDataSource
import javax.inject.Inject

class AppSharedPrefsRepository @Inject constructor(
    private val appSharedPrefsDataSource: AppSharedPrefsDataSource,
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