package com.therxmv.dirolreader.ui.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.dirolreader.domain.usecase.ProfileViewModelUseCase
import com.therxmv.dirolreader.ui.profile.viewmodel.utils.AppBarState
import com.therxmv.dirolreader.ui.profile.viewmodel.utils.ProfileUiEvent
import com.therxmv.dirolreader.ui.profile.viewmodel.utils.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val useCases: ProfileViewModelUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    private var client: Client? = useCases.getClientUseCase()

    init {
        setUpAppBar()
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.LogOut -> {
                logOut(event.onComplete)
            }
        }
    }

    private fun logOut(onComplete: () -> Unit) {
        client?.send(TdApi.LogOut()) {}
        client?.close()
        onComplete()
    }

    private fun setUpAppBar() {
        viewModelScope.launch {
            val user = useCases.getCurrentUserUseCase(client)

            _state.value = _state.value.copy(
                appBarState = AppBarState(
                    avatarPath = user.avatarPath,
                    userName = "${user.firstName} ${user.lastName}",
                )
            )
        }
    }
}