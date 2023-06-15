package com.therxmv.dirolreader.ui.news

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.usecase.NewsViewModelUseCases
import com.therxmv.dirolreader.ui.news.utils.NewsPostUiState
import com.therxmv.dirolreader.ui.news.utils.NewsUiEvent
import com.therxmv.dirolreader.ui.news.utils.NewsUiState
import com.therxmv.dirolreader.ui.news.utils.ToolbarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val useCases: NewsViewModelUseCases,
): ViewModel() {
    private val _state = MutableStateFlow(NewsUiState())
    val state = _state.asStateFlow()

    val postState = mutableStateMapOf<Long, NewsPostUiState>()

    private var client: Client? = useCases.getClientUseCase()

    var news: Flow<PagingData<MessageModel>>? = null

    init {
        loadChannels(null)
    }

    fun onEvent(event: NewsUiEvent) {
        when(event) {
            is NewsUiEvent.UpdateRating -> {
                viewModelScope.launch {
                    useCases.updateChannelRatingUseCase(event.id, event.num)
                }
            }
            is NewsUiEvent.MarkAsRead -> {
                client?.send(TdApi.ViewMessages(
                    event.channelId,
                    0,
                    longArrayOf(event.messageId),
                    true
                )) {}
            }
            is NewsUiEvent.LoadPhoto -> {
                loadMessagePhoto(event.messageId, event.photoId)
            }
        }
    }

    private fun loadMessagePhoto(messageId: Long, photoId: Int) {
        viewModelScope.launch {
            val path = useCases.getMessagePhotoUseCase(client, photoId)
            postState[messageId] = postState[messageId]?.copy(photoPath = path) ?: NewsPostUiState(photoPath = path)
        }
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
    fun loadChannels(onRefresh: (() -> Unit)?) {
        _state.value = _state.value.copy(
            isLoaded = false
        )

        viewModelScope.launch {
            delay(500)
            useCases.getRemoteChannelsIdsUseCase(client).collectLatest { list ->
                setToolbar(list.filter { it.unreadCount > 0 }.size)
                useCases.addChannelToLocaleUseCase(list)
            }

            if(news == null) {
                news = useCases.getMessagePagingUseCase(client).cachedIn(viewModelScope)
            }
            else {
                onRefresh?.let { it() }
            }
        }

        _state.value = _state.value.copy(
            isLoaded = true
        )
    }

    private fun setToolbar(unreadCount: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                toolbarState = loadToolbarInfo(unreadCount)
            )
        }
    }
}