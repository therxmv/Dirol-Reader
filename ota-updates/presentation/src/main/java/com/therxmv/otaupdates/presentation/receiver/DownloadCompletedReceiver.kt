package com.therxmv.otaupdates.presentation.receiver

import android.app.DownloadManager.EXTRA_DOWNLOAD_ID
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.edit
import com.therxmv.common.SharedPrefs.SHARED_PREFS
import com.therxmv.common.SharedPrefs.SHARED_PREFS_IS_UPDATE_DOWNLOADED

class DownloadCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.DOWNLOAD_COMPLETE") {
            val sharedPrefs = context?.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

            val id = intent.getLongExtra(EXTRA_DOWNLOAD_ID, -1L)

            sharedPrefs?.edit(commit = true) {
                putBoolean(SHARED_PREFS_IS_UPDATE_DOWNLOADED, id != -1L)
            }
        }
    }
}