package com.therxmv.dirolreader.utils.handlers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Chat

class UpdateHandler: Client.ResultHandler {
    companion object {
        private val _authorizationState = MutableStateFlow<TdApi.AuthorizationState?>(null)
        val authorizationState = _authorizationState.asSharedFlow()
    }

    override fun onResult(`object`: TdApi.Object?) {
        when(`object`?.constructor) {
            TdApi.UpdateAuthorizationState.CONSTRUCTOR -> {
                _authorizationState.value = (`object` as TdApi.UpdateAuthorizationState).authorizationState
            }
            TdApi.UpdateNewChat.CONSTRUCTOR -> {}
        }
    }
}