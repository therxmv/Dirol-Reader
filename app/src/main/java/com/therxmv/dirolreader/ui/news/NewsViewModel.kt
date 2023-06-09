package com.therxmv.dirolreader.ui.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.usecase.NewsViewModelUseCases
import com.therxmv.dirolreader.ui.news.utils.NewsUiState
import com.therxmv.dirolreader.ui.news.utils.ToolbarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.ChatPhotos
import org.drinkless.td.libcore.telegram.TdApi.File
import org.drinkless.td.libcore.telegram.TdApi.User
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val useCases: NewsViewModelUseCases,
): ViewModel() {
    private val _state = MutableStateFlow(NewsUiState())
    val state = _state.asStateFlow()

    private var client: Client? = null

    init {
        Log.d("rozmi", "news vm init")
        client = useCases.getClientUseCase()
        getUserAvatar()
        viewModelScope.launch {
            useCases.addChannelToLocaleUseCase(
                useCases.getRemoteChannelsIdsUseCase(client).map {
                    ChannelModel(it)
                }
            )
            delay(1000)
            Log.d("rozmi", useCases.getLocaleChannelsUseCase().toString())
        }
    }

    private suspend fun getToolbarState(): ToolbarState {
        val user = useCases.getCurrentUserUseCase(client)
        val avatarPath = useCases.getCurrentUserAvatarUseCase(client, user)

        return ToolbarState(
            avatarPath = avatarPath,
            userName = "${user.firstName} ${user.lastName}"
        )
    }

    private fun getUserAvatar() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                toolbarState = getToolbarState()
            )
        }
    }
}