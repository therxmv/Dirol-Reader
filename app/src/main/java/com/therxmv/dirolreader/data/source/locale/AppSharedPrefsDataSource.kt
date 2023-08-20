package com.therxmv.dirolreader.data.source.locale

import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.therxmv.constants.SharedPrefs.SHARED_PREFS
import com.therxmv.constants.SharedPrefs.SHARED_PREFS_CHANNELS_RATING
import com.therxmv.constants.SharedPrefs.SHARED_PREFS_IS_AUTO_DELETE_ENABLED
import com.therxmv.constants.SharedPrefs.SHARED_PREFS_IS_DYNAMIC
import com.therxmv.constants.SharedPrefs.SHARED_PREFS_IS_UPDATE_DOWNLOADED

class AppSharedPrefsDataSource(
    private val context: Context
) {
    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    var isDynamic: Boolean
        get() {
            return sharedPrefs.getBoolean(SHARED_PREFS_IS_DYNAMIC, false)
        }
        set(value) {
            sharedPrefs.edit(true) {
                putBoolean(SHARED_PREFS_IS_DYNAMIC, value)
            }
        }

    var isAutoDeleteEnabled: Boolean
        get() {
            return sharedPrefs.getBoolean(SHARED_PREFS_IS_AUTO_DELETE_ENABLED, false)
        }
        set(value) {
            sharedPrefs.edit(true) {
                putBoolean(SHARED_PREFS_IS_AUTO_DELETE_ENABLED, value)
            }
        }

    var channelsRating: MutableMap<Long, Int>
        get() {
            val str = sharedPrefs.getString(SHARED_PREFS_CHANNELS_RATING, "")

            if(str.isNullOrEmpty()) return mutableMapOf()

            val type = TypeToken.getParameterized(Map::class.java, Long::class.java, Int::class.java).type

            return Gson().fromJson(str, type)
        }
        set(value) {
            sharedPrefs.edit(true) {
                val str = if(value.isEmpty()) "" else Gson().toJson(value)

                putString(SHARED_PREFS_CHANNELS_RATING, str)
            }
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
            if(key == SHARED_PREFS_IS_UPDATE_DOWNLOADED) {
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