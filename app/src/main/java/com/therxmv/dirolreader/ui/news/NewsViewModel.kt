package com.therxmv.dirolreader.ui.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.usecase.NewsViewModelUseCases
import com.therxmv.dirolreader.ui.news.utils.NewsUiState
import com.therxmv.dirolreader.ui.news.utils.ToolbarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.Client
import javax.inject.Inject

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
        loadChannels()
    }

    private suspend fun loadToolbarInfo(unreadCount: Int): ToolbarState {
        val user = useCases.getCurrentUserUseCase(client)
        val avatarPath = useCases.getCurrentUserAvatarUseCase(client, user)

        return ToolbarState(
            avatarPath = avatarPath,
            userName = "${user.firstName} ${user.lastName}",
            unreadChannels = unreadCount
        )
    }
    private fun loadChannels() {
        viewModelScope.launch {
            useCases.getRemoteChannelsIdsUseCase(client).collectLatest { list ->
                setToolbar(list.filter { it.second > 0 }.size)
                useCases.addChannelToLocaleUseCase(list.map { ChannelModel(id = it.first, unreadCount = it.second) })
            }
            Log.d("rozmi", useCases.getLocaleChannelsUseCase().toString())
        }
    }

    private fun setToolbar(unreadCount: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                toolbarState = loadToolbarInfo(unreadCount)
            )
        }
    }
}