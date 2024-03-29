package com.therxmv.dirolreader.domain.usecase

import android.content.Context
import android.os.Build
import com.therxmv.dirolreader.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import org.drinkless.td.libcore.telegram.TdApi
import java.util.Locale
import javax.inject.Inject

class GetTdLibParametersUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() = TdApi.TdlibParameters().apply {
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