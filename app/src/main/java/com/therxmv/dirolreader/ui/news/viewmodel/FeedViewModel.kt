package com.therxmv.dirolreader.ui.news.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.therxmv.common.Rating.STAR_RATING
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.usecase.NewsViewModelUseCases
import com.therxmv.dirolreader.ui.news.view.post.ChannelUiData
import com.therxmv.dirolreader.ui.news.view.post.NewsPostUiData
import com.therxmv.dirolreader.ui.news.viewmodel.utils.FeedUiState
import com.therxmv.dirolreader.ui.news.viewmodel.utils.NewsUiEvent
import com.therxmv.dirolreader.ui.news.viewmodel.utils.ToolbarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val useCases: NewsViewModelUseCases,
    @Named("IO") private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeedUiState>(FeedUiState.InitialState)
    val uiState = _uiState.asStateFlow()

    private val _starredChannels = MutableStateFlow<List<Long>>(emptyList())
    val starredChannels = _starredChannels.asStateFlow()

    private val readMessages = mutableListOf<Long>()

    val news = useCases.getNewsPaging()
        .map { paging ->
            paging.map { it.toPresentation() }
        }
        .cachedIn(viewModelScope)

    init {
        toolbarDataObserver()
    }

    private fun toolbarDataObserver() {
        viewModelScope.launch(ioDispatcher) {
            useCases.getUnreadChannelsFlow().collectLatest { unreadList ->
                val toolbarData = loadToolbarInfo(unreadList.size)

                unreadList
                    .filter { it.rating >= STAR_RATING }
                    .map { it.id }
                    .also { newList ->
                        _starredChannels.update { it + newList }
                    }

                _uiState.update {
                    FeedUiState.Ready(
                        toolbarState = toolbarData,
                    )
                }
            }
        }
    }

    private suspend fun loadToolbarInfo(unreadCount: Int): ToolbarState {
        val user = useCases.getCurrentUser()

        return ToolbarState(
            avatarPath = user.avatarPath,
            userName = "${user.firstName} ${user.lastName}",
            unreadChannels = unreadCount
        )
    }

    fun onEvent(event: NewsUiEvent) {
        when (event) {
            is NewsUiEvent.Like -> {
                val number = when (event.isLiked) {
                    true -> -1 // remove like
                    false -> 2 // remove dislike, add like
                    else -> 1 // add like
                }
                updateChannelRating(event.channelId, number)
            }

            is NewsUiEvent.Dislike -> {
                val number = when (event.isLiked) {
                    true -> -2 // remove like, add dislike
                    false -> 1 // remove dislike
                    else -> -1 // add dislike
                }
                updateChannelRating(event.channelId, number)
            }

            is NewsUiEvent.StarChannel -> {
                val number = when (event.isStarred) {
                    true -> -100
                    false -> 100
                }
                updateChannelRating(event.channelId, number)
                _starredChannels.update {
                    if (it.contains(event.channelId)) {
                        it - event.channelId
                    } else {
                        it + event.channelId
                    }
                }
            }

            is NewsUiEvent.MarkAsRead -> {
                if (readMessages.contains(event.messageId).not()) {
                    readMessages.add(event.messageId)
                    useCases.markMessageAsRead(event.messageId, event.channelId)
                }
            }
        }
    }

    private fun updateChannelRating(channelId: Long, number: Int) {
        viewModelScope.launch(ioDispatcher) {
            useCases.updateChannelRating(channelId, number)
        }
    }

    suspend fun loadMessageMedia(mediaId: Int) = withContext(ioDispatcher) {
        useCases.downloadMediaAndGetPath(mediaId)
    }

    private fun MessageModel.toPresentation() = NewsPostUiData(
        id = this.id,
        text = this.text,
        mediaList = this.mediaList?.toPersistentList(),
        channelData = ChannelUiData(
            id = this.channelData.id,
            name = this.channelData.name,
            avatarPath = this.channelData.avatarPath,
            postTime = useCases.getReadablePostTime(this.timestamp),
        )
    )
}