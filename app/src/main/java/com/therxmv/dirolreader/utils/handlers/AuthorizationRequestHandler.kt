package com.therxmv.dirolreader.utils.handlers

import android.util.Log
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class AuthorizationRequestHandler: Client.ResultHandler {
    override fun onResult(`object`: TdApi.Object?) {
        when (`object`!!.constructor) {
            TdApi.Error.CONSTRUCTOR -> Log.d("authError", "Receive an error:\n$`object`")
            TdApi.Ok.CONSTRUCTOR -> {}
            else -> Log.d("authError", "Receive wrong response from TDLib: $`object`")
        }
    }
}