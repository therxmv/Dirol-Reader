package com.therxmv.dirolreader.ui.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthInputState
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthState
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthState.CODE
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthState.ERROR
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthState.PASSWORD
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthState.PHONE
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthState.PROCESSING
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthState.READY
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthState.START
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.drinkless.tdlib.TdApi.AuthorizationState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val client: Client,
) : ViewModel() {

    private val _authState = MutableStateFlow(START)
    val authState = _authState.asStateFlow()

    private val _inputState = MutableStateFlow(AuthInputState())
    val inputState = _inputState.asStateFlow()

    init {
        getAuthorizationState()
    }

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.ConfirmInput -> {
                sendInput(_authState.value, _inputState.value.inputValue)
                _authState.update { PROCESSING }
            }

            is AuthUiEvent.OnValueChange -> {
                _inputState.update {
                    it.copy(
                        inputValue = event.value,
                        isValidInput = true,
                    )
                }
            }
        }
    }

    private fun sendInput(authState: AuthState, input: String) {
        when (authState) {
            PHONE -> client.send(TdApi.SetAuthenticationPhoneNumber(input, null)) {
                getAuthorizationState(authState)
            }

            CODE -> client.send(TdApi.CheckAuthenticationCode(input)) {
                getAuthorizationState(authState)
            }

            PASSWORD -> client.send(TdApi.CheckAuthenticationPassword(input)) {
                getAuthorizationState(authState)
            }

            else -> {}
        }
    }

    private fun getAuthorizationState(previousState: AuthState = _authState.value) {
        client.send(TdApi.GetAuthorizationState()) {
            onAuthorizationStateUpdated(it as AuthorizationState, previousState)
        }
    }

    private fun onAuthorizationStateUpdated(authorizationState: AuthorizationState, previousState: AuthState) {
        when (authorizationState) {
            is TdApi.AuthorizationStateWaitPhoneNumber -> {
                Log.d("rozmi_authState", "AuthState.PHONE")
                setErrorIfSameState(PHONE, previousState)
                _authState.update { PHONE }
            }

            is TdApi.AuthorizationStateWaitCode -> {
                Log.d("rozmi_authState", "AuthState.CODE")
                setErrorIfSameState(CODE, previousState)
                _authState.update { CODE }
            }

            is TdApi.AuthorizationStateWaitPassword -> {
                Log.d("rozmi_authState", "AuthState.PASSWORD")
                setErrorIfSameState(PASSWORD, previousState)
                _authState.update { PASSWORD }
            }

            is TdApi.AuthorizationStateReady -> {
                Log.d("rozmi_authState", "AuthState.READY")
                _authState.update { READY }
            }

            else -> {
                Log.d("rozmi_authState", authorizationState.toString())
                _authState.update { ERROR }
            }
        }
    }

    private fun setErrorIfSameState(state: AuthState, previousState: AuthState) {
        val isValid = state != previousState

        _inputState.update {
            it.copy(
                inputValue = it.inputValue.takeUnless { isValid }.orEmpty(),
                isValidInput = isValid,
            )
        }
    }
}