package com.therxmv.dirolreader.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.therxmv.dirolreader.data.models.MediaModel
import com.therxmv.dirolreader.data.models.MediaType
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.usecase.NewsViewModelUseCases
import com.therxmv.dirolreader.ui.news.utils.NewsUiEvent
import com.therxmv.dirolreader.ui.news.utils.NewsUiState
import com.therxmv.dirolreader.ui.news.utils.ToolbarState
import com.therxmv.sharedpreferences.model.ChannelRatingModel
import com.therxmv.sharedpreferences.model.ChannelsRatingListModel
import com.therxmv.sharedpreferences.repository.AppSharedPrefsRepository
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
    private val appSharedPrefsRepository: AppSharedPrefsRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(NewsUiState())
    val state = _state.asStateFlow()

    private val channelsRating = ChannelsRatingListModel()

    private val readMessages = mutableListOf<Long>()

    private var client: Client? = null

    var news: Flow<PagingData<MessageModel>>? = null

    init {
        client = useCases.getClientUseCase()
        loadChannels(null)
    }

    fun onEvent(event: NewsUiEvent) {
        when (event) {
            is NewsUiEvent.UpdateRating -> {
                val itemIndex = channelsRating.list.indexOfFirst { it.channelId == event.id }

                if (itemIndex == -1) {
                    channelsRating.list.add(ChannelRatingModel(event.id, event.num))
                } else {
                    val item = channelsRating.list[itemIndex]

                    channelsRating.list[itemIndex] = ChannelRatingModel(event.id, item.rating + event.num)
                }

                appSharedPrefsRepository.channelsRating = channelsRating
            }

            is NewsUiEvent.MarkAsRead -> {
                if (readMessages.contains(event.messageId).not()) {
                    readMessages.add(event.messageId)
                    client?.send(
                        TdApi.ViewMessages(
                            event.channelId,
                            0,
                            longArrayOf(event.messageId),
                            true
                        )
                    ) {}
                }
            }
        }
    }

    private fun updateRating() {
        appSharedPrefsRepository.channelsRating.list.forEach {
            viewModelScope.launch {
                useCases.updateChannelRatingUseCase(it.channelId, it.rating)
            }
        }
        channelsRating.list.clear()
        appSharedPrefsRepository.channelsRating = channelsRating
    }

    suspend fun loadMessageMedia(mediaList: List<MediaModel>, loadVideo: Boolean) =
        mediaList.map {
            if (it.type == MediaType.VIDEO && loadVideo.not()) null else useCases.getMessageMediaUseCase(client, it.id)
        }

    private suspend fun loadToolbarInfo(unreadCount: Int): ToolbarState {
        val user = useCases.getCurrentUserUseCase(client)

        return ToolbarState(
            avatarPath = user.avatarPath,
            userName = "${user.firstName} ${user.lastName}",
            unreadChannels = unreadCount
        )
    }

    fun loadChannels(onRefresh: (() -> Unit)?) {
        updateRating()

        _state.value = _state.value.copy(
            isLoaded = false
        )

        viewModelScope.launch {
            delay(1000) // fix for issue with instant loading
            useCases.getRemoteChannelsIdsUseCase(client).collectLatest { list ->
                setToolbar(list.filter { it.unreadCount > 0 }.size)

                val res = useCases.addChannelToLocaleUseCase(list)
                if (res == list.size) {
                    if (news == null) {
                        news = useCases.getMessagePagingUseCase(client).cachedIn(viewModelScope)
                    } else {
                        onRefresh?.let { it() }
                    }
                }
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