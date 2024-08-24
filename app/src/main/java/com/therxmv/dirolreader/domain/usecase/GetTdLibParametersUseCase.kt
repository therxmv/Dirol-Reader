package com.therxmv.dirolreader.domain.usecase

import android.content.Context
import android.os.Build
import com.therxmv.dirolreader.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import org.drinkless.tdlib.TdApi
import java.util.Locale
import javax.inject.Inject

class GetTdLibParametersUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() = TdApi.SetTdlibParameters(
        /* useTestDc = */ false, // TODO 3 test doesn't work or idk how
        /* databaseDirectory = */ context.filesDir.absolutePath,
        /* filesDirectory = */ "",
        /* databaseEncryptionKey = */ byteArrayOf(),
        /* useFileDatabase = */ true,
        /* useChatInfoDatabase = */ true,
        /* useMessageDatabase = */ true,
        /* useSecretChats = */ true,
        /* apiId = */ BuildConfig.API_ID,
        /* apiHash = */ BuildConfig.API_HASH,
        /* systemLanguageCode = */ Locale.getDefault().toLanguageTag(),
        /* deviceModel = */ Build.MODEL,
        /* systemVersion = */ "",
        /* applicationVersion = */ BuildConfig.VERSION_NAME,
        )
}