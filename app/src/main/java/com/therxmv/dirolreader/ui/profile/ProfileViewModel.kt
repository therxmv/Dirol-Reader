package com.therxmv.dirolreader.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.dirolreader.domain.usecase.ProfileViewModelUseCase
import com.therxmv.dirolreader.ui.profile.utils.AppBarState
import com.therxmv.dirolreader.ui.profile.utils.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val useCases: ProfileViewModelUseCase
): ViewModel() {
    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    private var client: Client? = useCases.getClientUseCase()

    init {
        setAppBar()
    }

    fun logOut() {
        client?.send(TdApi.LogOut()) {}
        client?.close()
    }

    private fun setAppBar() {
        viewModelScope.launch {
            val user = useCases.getCurrentUserUseCase(client)
            val avatarPath = useCases.getCurrentUserAvatarUseCase(client, user)

            _state.value = _state.value.copy(
                appBarState = AppBarState(
                    avatarPath = avatarPath,
                    userName = "${user.firstName} ${user.lastName}",
                )
            )
        }
    }
}