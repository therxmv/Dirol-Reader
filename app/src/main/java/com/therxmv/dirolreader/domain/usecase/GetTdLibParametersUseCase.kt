package com.therxmv.dirolreader.domain.usecase

import android.content.Context
import android.os.Build
import com.therxmv.dirolreader.BuildConfig
import org.drinkless.td.libcore.telegram.TdApi.TdlibParameters
import java.util.*

class GetTdLibParametersUseCase(private val context: Context) {
    fun invoke() = TdlibParameters().apply {
        apiId = BuildConfig.API_ID
        apiHash = BuildConfig.API_HASH
        databaseDirectory = context.filesDir.absolutePath
        useMessageDatabase = true
        useSecretChats = false
        systemLanguageCode = Locale.getDefault().toLanguageTag()
        deviceModel = Build.MODEL
        applicationVersion = BuildConfig.VERSION_NAME
        enableStorageOptimizer = true
    }
}