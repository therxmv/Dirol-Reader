package com.therxmv.dirolreader.ui.auth.viewmodel

import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import com.therxmv.common.Path.FILES_PATH
import com.therxmv.dirolreader.domain.usecase.AuthViewModelUseCases
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
import com.therxmv.sharedpreferences.repository.AppSharedPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationState
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val useCases: AuthViewModelUseCases,
    private val appSharedPrefsRepository: AppSharedPrefsRepository,
) : ViewModel() {

    private val _authState = MutableStateFlow(START)
    val authState = _authState.asStateFlow()

    private val _inputState = MutableStateFlow(AuthInputState())
    val inputState = _inputState.asStateFlow()

    private var client: Client? = null

    init {
        createClient()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("rozmi", "cleared")
        clearCache()
    }

    private fun clearCache() {
        if (appSharedPrefsRepository.isAutoDeleteEnabled) {
            File(FILES_PATH).listFiles()?.forEach { folder ->
                if (folder.isDirectory) {
                    folder.listFiles()?.firstOrNull { file ->
                        file.name.contains(".nomedia")
                    }?.let {
                        folder.listFiles()?.forEach { elem ->
                            if (!elem.name.contains(".nomedia")) {
                                elem.delete()
                            }
                        }
                    }
                }
            }

            val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            downloads.listFiles()?.forEach {
                if (it.isFile && it.name.contains("Dirol-Reader")) {
                    it.delete()
                }
            }
        }
    }

    private fun createClient() {
        client = useCases.createClientUseCase(ClientUpdateHandler())
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
            PHONE -> client?.send(TdApi.SetAuthenticationPhoneNumber(input, null)) {
                getAuthorizationState(authState)
            }

            CODE -> client?.send(TdApi.CheckAuthenticationCode(input)) {
                getAuthorizationState(authState)
            }

            PASSWORD -> client?.send(TdApi.CheckAuthenticationPassword(input)) {
                getAuthorizationState(authState)
            }

            else -> {}
        }
    }

    private fun getAuthorizationState(previousState: AuthState = _authState.value) {
        client?.send(TdApi.GetAuthorizationState()) {
            it as AuthorizationState
            onAuthorizationStateUpdated(it, previousState)
        }
    }

    private fun onAuthorizationStateUpdated(authorizationState: AuthorizationState, previousState: AuthState) {
        when (authorizationState) {
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

    inner class ClientUpdateHandler : Client.ResultHandler {
        override fun onResult(`object`: TdApi.Object?) {}
    }
}