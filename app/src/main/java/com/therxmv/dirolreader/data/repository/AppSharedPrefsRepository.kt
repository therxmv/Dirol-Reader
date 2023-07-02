package com.therxmv.dirolreader.data.repository

import com.therxmv.dirolreader.data.source.locale.AppSharedPrefsDataSource

class AppSharedPrefsRepository(
    private val appSharedPrefsDataSource: AppSharedPrefsDataSource
) {
    var isDynamic: Boolean
        get() {
            return appSharedPrefsDataSource.isDynamic
        }
        set(value) {
            appSharedPrefsDataSource.isDynamic = value
        }

    var isAutoDeleteEnabled: Boolean
        get() {
            return appSharedPrefsDataSource.isAutoDeleteEnabled
        }
        set(value) {
            appSharedPrefsDataSource.isAutoDeleteEnabled = value
        }
}