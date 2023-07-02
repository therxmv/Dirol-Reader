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
        createClient()
    }

    private fun createClient() {
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

    fun disableError() {
        _state.value = _state.value.copy(
            isValidInput = true
        )
    }

    private fun onAuthorizationStateUpdated(authorizationState: AuthorizationState?) {
        when(authorizationState!!) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> {
                Log.d("rozmi_authState", "AuthState.PARAMS")
                client?.send(TdApi.SetTdlibParameters(useCases.getTdLibParametersUseCase())) {
                    getAuthorizationState()
                }
            }
            is TdApi.AuthorizationStateWaitEncryptionKey -> {
                Log.d("rozmi_authState", "AuthState.KEY")
                client?.send(TdApi.CheckDatabaseEncryptionKey()) {
                    getAuthorizationState()
                }
            }
            is TdApi.AuthorizationStateWaitPhoneNumber -> {
                Log.d("rozmi_authState", "AuthState.PHONE")
                _state.value = _state.value.copy(
                    authState = AuthState.PHONE,
                    isValidInput = _state.value.authState != AuthState.PHONE
                )
            }
            is TdApi.AuthorizationStateWaitCode -> {
                Log.d("rozmi_authState", "AuthState.CODE")
                _state.value = _state.value.copy(
                    authState = AuthState.CODE,
                    isValidInput = _state.value.authState != AuthState.CODE
                )
            }
            is TdApi.AuthorizationStateWaitPassword -> {
                Log.d("rozmi_authState", "AuthState.PASSWORD")
                _state.value = _state.value.copy(
                    authState = AuthState.PASSWORD,
                    isValidInput = _state.value.authState != AuthState.PASSWORD
                )
            }
            is TdApi.AuthorizationStateReady -> {
                Log.d("rozmi_authState", "AuthState.READY")
                _state.value = _state.value.copy(authState = AuthState.READY)
                onCleared()
            }
            is TdApi.AuthorizationStateLoggingOut -> {
                Log.d("rozmi_authState", "AuthState.LOGGING_OUT")
                createClient()
            }
            is TdApi.AuthorizationStateClosing -> {
                Log.d("rozmi_authState", "AuthState.CLOSING")
                createClient()
            }
            is TdApi.AuthorizationStateClosed -> {
                Log.d("rozmi_authState", "AuthState.CLOSED")
                createClient()
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