package com.therxmv.dirolreader.ui.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.therxmv.dirolreader.data.models.MediaModel
import com.therxmv.dirolreader.data.models.MediaType
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.usecase.NewsViewModelUseCases
import com.therxmv.dirolreader.ui.news.viewmodel.utils.NewsUiEvent
import com.therxmv.dirolreader.ui.news.viewmodel.utils.NewsUiState
import com.therxmv.dirolreader.ui.news.viewmodel.utils.ToolbarData
import com.therxmv.sharedpreferences.model.ChannelRatingModel
import com.therxmv.sharedpreferences.model.ChannelsRatingListModel
import com.therxmv.sharedpreferences.repository.AppSharedPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val useCases: NewsViewModelUseCases,
    private val appSharedPrefsRepository: AppSharedPrefsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _toolbarDataState = MutableStateFlow(ToolbarData())
    val toolbarDataState = _toolbarDataState.asStateFlow()

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
                updateChannelRatingById(event.id, event.num)
            }

            is NewsUiEvent.MarkAsRead -> {
                readMessageIfNotReadYet(event.channelId, event.messageId)
            }
        }
    }

    private fun updateChannelRatingById(id: Long, num: Int) {
        val itemIndex = channelsRating.list.indexOfFirst { it.channelId == id }

        if (itemIndex == -1) {
            channelsRating.list.add(ChannelRatingModel(id, num))
        } else {
            val item = channelsRating.list[itemIndex]
            val newRating = item.rating + num

            channelsRating.list[itemIndex] = ChannelRatingModel(id, newRating)
        }

        appSharedPrefsRepository.channelsRating = channelsRating
    }

    private fun readMessageIfNotReadYet(channelId: Long, messageId: Long) {
        if (readMessages.contains(messageId).not()) {
            readMessages.add(messageId)
            client?.send(
                TdApi.ViewMessages(
                    channelId,
                    0,
                    longArrayOf(messageId),
                    true
                )
            ) {}
        }
    }

    /**
     * Used to not update rating instantly
     * only during refresh or next app launch
     */
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
            if (it.type == MediaType.VIDEO && loadVideo.not()) {
                null
            } else {
                useCases.getMessageMediaUseCase(client, it.id)
            }
        }

    fun loadChannels(onRefresh: (() -> Unit)?) {
        updateRating()

        _uiState.update { NewsUiState.Loading }

        viewModelScope.launch {
            delay(1000) // fix for issue with instant loading
            useCases.getRemoteChannelsIdsUseCase(client).collectLatest { list ->
                setToolbarState(list)

                val result = useCases.addChannelToLocaleUseCase(list)
                if (result == list.size) {
                    if (news == null) {
                        news = useCases.getMessagePagingUseCase(client).cachedIn(viewModelScope)
                    } else {
                        onRefresh?.invoke()
                    }
                }
            }
        }

        _uiState.update { NewsUiState.Ready }
    }

    private suspend fun setToolbarState(channels: List<ChannelModel>) {
        val unreadCount = channels.filter { it.unreadCount > 0 }.size

        _toolbarDataState.update {
            loadToolbarInfo(unreadCount)
        }
    }

    private suspend fun loadToolbarInfo(unreadCount: Int): ToolbarData {
        val user = useCases.getCurrentUserUseCase(client)

        return ToolbarData(
            avatarPath = user.avatarPath,
            userName = "${user.firstName} ${user.lastName}",
            unreadChannels = unreadCount
        )
    }
}