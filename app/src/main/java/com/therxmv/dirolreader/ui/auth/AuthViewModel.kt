package com.therxmv.dirolreader.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.dirolreader.domain.usecase.GetClientUseCase
import com.therxmv.dirolreader.domain.usecase.GetTdLibParametersUseCase
import com.therxmv.dirolreader.utils.AuthState
import com.therxmv.dirolreader.utils.handlers.AuthorizationRequestHandler
import com.therxmv.dirolreader.utils.handlers.UpdateHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getClientUseCase: GetClientUseCase,
    private val getTdLibParametersUseCase: GetTdLibParametersUseCase,
): ViewModel() {
    private var client: Client? = null

    private val _authState = MutableSharedFlow<AuthState>()
    val authState = _authState.asSharedFlow()

    fun getClient() {
        client = getClientUseCase.invoke(UpdateHandler())

        viewModelScope.launch {
            UpdateHandler.authorizationState.collectLatest {
                onAuthorizationStateUpdated(it)
            }
        }
    }

    fun confirmPhone(phone: String) {
        client?.send(TdApi.SetAuthenticationPhoneNumber(phone, null), AuthorizationRequestHandler())
    }

    fun confirmCode(code: String) {
        client?.send(TdApi.CheckAuthenticationCode(code), AuthorizationRequestHandler())
    }

    fun confirmPassword(password: String) {
        client?.send(TdApi.CheckAuthenticationPassword(password), AuthorizationRequestHandler())
    }

    private fun onAuthorizationStateUpdated(authorizationState: TdApi.AuthorizationState?) {
        when(authorizationState?.constructor) {
            TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                client?.send(TdApi.SetTdlibParameters(getTdLibParametersUseCase.invoke()), AuthorizationRequestHandler())
            }
            TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> {
                client?.send(TdApi.CheckDatabaseEncryptionKey(), AuthorizationRequestHandler())
            }
            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> {
                viewModelScope.launch {
                    _authState.emit(AuthState.PHONE)
                }
                Log.d("authError", "Phone successful")
            }
            TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> {
                viewModelScope.launch {
                    _authState.emit(AuthState.CODE)
                }
                Log.d("authError", "Code successful")
            }
            TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> {
                viewModelScope.launch {
                    _authState.emit(AuthState.PASSWORD)
                }
                Log.d("authError", "Password successful")
            }
            TdApi.AuthorizationStateReady.CONSTRUCTOR -> {
                viewModelScope.launch {
                    _authState.emit(AuthState.READY)
                }
                Log.d("authError", "successful")
            }
            else -> {
                viewModelScope.launch {
                    _authState.emit(AuthState.ERROR)
                }
            }
        }
    }
}