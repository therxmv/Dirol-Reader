package com.therxmv.dirolreader.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.dirolreader.domain.usecase.AuthViewModelUseCases
import com.therxmv.dirolreader.ui.auth.utils.AuthState
import com.therxmv.dirolreader.ui.auth.utils.AuthUiEvent
import com.therxmv.dirolreader.ui.auth.utils.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val useCases: AuthViewModelUseCases
): ViewModel() {
    private val _state = MutableStateFlow(AuthUiState())
    val state = _state.asStateFlow()

    private var client: Client? = null

    init {
        Log.d("rozmi", "auth vm init")
        client = useCases.createClientUseCase(ClientUpdateHandler())
        getAuthorizationState()
    }

    fun onEvent(event: AuthUiEvent) {
        when(event) {
            is AuthUiEvent.ConfirmInput -> {
                sendInput(event.authState, event.input)
            }
        }
    }

    private fun sendInput(authState: AuthState, input: String) {
        when(authState) {
            AuthState.PHONE -> client?.send(TdApi.SetAuthenticationPhoneNumber(input, null)) { getAuthorizationState() }
            AuthState.CODE -> client?.send(TdApi.CheckAuthenticationCode(input)) { getAuthorizationState() }
            AuthState.PASSWORD -> client?.send(TdApi.CheckAuthenticationPassword(input)) { getAuthorizationState() }
            else -> {}
        }
    }

    private fun getAuthorizationState() {
        client?.send(TdApi.GetAuthorizationState()) {
            it as AuthorizationState
            onAuthorizationStateUpdated(it)
        }
    }

    private fun onAuthorizationStateUpdated(authorizationState: AuthorizationState?) {
        when(authorizationState?.constructor) {
            TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                Log.d("rozmi_authState", "AuthState.PARAMS")
                client?.send(TdApi.SetTdlibParameters(useCases.getTdLibParametersUseCase())) {
                    getAuthorizationState()
                }
            }
            TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> {
                Log.d("rozmi_authState", "AuthState.KEY")
                client?.send(TdApi.CheckDatabaseEncryptionKey()) {
                    getAuthorizationState()
                }
            }
            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> {
                Log.d("rozmi_authState", "AuthState.PHONE")
                _state.value = _state.value.copy(authState = AuthState.PHONE)
            }
            TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> {
                Log.d("rozmi_authState", "AuthState.CODE")
                _state.value = _state.value.copy(authState = AuthState.CODE)
            }
            TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> {
                Log.d("rozmi_authState", "AuthState.PASSWORD")
                _state.value = _state.value.copy(authState = AuthState.PASSWORD)
            }
            TdApi.AuthorizationStateReady.CONSTRUCTOR -> {
                Log.d("rozmi_authState", "AuthState.READY")
                _state.value = _state.value.copy(authState = AuthState.READY)
                onCleared()
            }
            else -> {
                Log.d("rozmi_authState", "AuthState.ERROR")
                _state.value = _state.value.copy(authState = AuthState.ERROR)
            }
        }
    }

    inner class ClientUpdateHandler: Client.ResultHandler {
        override fun onResult(`object`: TdApi.Object?) {}
    }
}