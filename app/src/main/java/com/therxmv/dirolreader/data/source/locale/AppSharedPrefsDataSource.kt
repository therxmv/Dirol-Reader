package com.therxmv.dirolreader.data.source.locale

import android.content.Context
import androidx.core.content.edit
import com.therxmv.dirolreader.utils.SHARED_PREFS
import com.therxmv.dirolreader.utils.SHARED_PREFS_IS_AUTO_DELETE_ENABLED
import com.therxmv.dirolreader.utils.SHARED_PREFS_IS_DYNAMIC

class AppSharedPrefsDataSource(
    private val context: Context
) {
    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    var isDynamic: Boolean
        get() {
            return sharedPrefs.getBoolean(SHARED_PREFS_IS_DYNAMIC, false)
        }
        set(value) {
            sharedPrefs.edit {
                putBoolean(SHARED_PREFS_IS_DYNAMIC, value)
                apply()
            }
        }

    var isAutoDeleteEnabled: Boolean
        get() {
            return sharedPrefs.getBoolean(SHARED_PREFS_IS_AUTO_DELETE_ENABLED, false)
        }
        set(value) {
            sharedPrefs.edit {
                putBoolean(SHARED_PREFS_IS_AUTO_DELETE_ENABLED, value)
                apply()
            }
        }
}