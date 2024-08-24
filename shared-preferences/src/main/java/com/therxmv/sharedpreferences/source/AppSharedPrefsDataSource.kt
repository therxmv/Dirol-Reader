package com.therxmv.sharedpreferences.source

import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.core.content.edit
import com.tencent.mmkv.MMKV
import com.therxmv.common.SharedPrefs.SHARED_PREFS
import com.therxmv.common.SharedPrefs.SHARED_PREFS_IS_AUTO_DELETE_ENABLED
import com.therxmv.common.SharedPrefs.SHARED_PREFS_IS_DYNAMIC
import com.therxmv.common.SharedPrefs.SHARED_PREFS_IS_UPDATE_DOWNLOADED
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppSharedPrefsDataSource @Inject constructor(
    @ApplicationContext context: Context,
) {

    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    private val mmkv = MMKV.defaultMMKV()

    var isDynamic: Boolean
        get() = mmkv.decodeBool(SHARED_PREFS_IS_DYNAMIC, false)
        set(value) {
            mmkv.encode(SHARED_PREFS_IS_DYNAMIC, value)
        }

    var isAutoDeleteEnabled: Boolean
        get() = mmkv.decodeBool(SHARED_PREFS_IS_AUTO_DELETE_ENABLED, false)
        set(value) {
            mmkv.encode(SHARED_PREFS_IS_AUTO_DELETE_ENABLED, value)
        }

    var isUpdateDownloaded: Boolean
        get() = sharedPrefs.getBoolean(SHARED_PREFS_IS_UPDATE_DOWNLOADED, false)
        set(value) {
            sharedPrefs.edit(true) {
                putBoolean(SHARED_PREFS_IS_UPDATE_DOWNLOADED, value)
            }
        }

    fun isUpdateDownloadedChangeListener(callback: (isDownloaded: Boolean) -> Unit) =
        OnSharedPreferenceChangeListener { _, key ->
            if (key == SHARED_PREFS_IS_UPDATE_DOWNLOADED) {
                callback(isUpdateDownloaded)
            }
        }

    fun registerChangeListener(listener: OnSharedPreferenceChangeListener) {
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterChangeListener(listener: OnSharedPreferenceChangeListener) {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener)
    }
}