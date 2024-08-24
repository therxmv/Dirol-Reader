package com.therxmv.dirolreader.ui.news.view.post

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.therxmv.dirolreader.domain.models.MediaModel
import com.therxmv.dirolreader.domain.models.MediaType
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
private fun PostWithTextPreview() {
    NewsPost(
        data = NewsPostUiData(
            id = 0,
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            mediaList = null,
            channelData = getChannelData(),
        ),
        loadMedia = ::loadMessageMedia,
        isStarred = false,
        onStarChannel = {},
        onLike = {},
        onDislike = {},
        markAsRead = {},
    )
}

@Preview
@Composable
private fun PostWithTextAndOnePhotoPreview() {
    NewsPost(
        data = NewsPostUiData(
            id = 0,
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            mediaList = persistentListOf(
                getMediaModel(),
            ),
            channelData = getChannelData(),
        ),
        loadMedia = ::loadMessageMedia,
        isStarred = false,
        onStarChannel = {},
        onLike = {},
        onDislike = {},
        markAsRead = {},
    )
}

@Preview
@Composable
private fun PostWithoutTextAndOnePhotoPreview() {
    NewsPost(
        data = NewsPostUiData(
            id = 0,
            text = "",
            mediaList = persistentListOf(
                getMediaModel()
            ),
            channelData = getChannelData(),
        ),
        loadMedia = ::loadMessageMedia,
        isStarred = true,
        onStarChannel = {},
        onLike = {},
        onDislike = {},
        markAsRead = {},
    )
}

@Preview
@Composable
private fun PostWithTextAndThreeMediaPreview() {
    NewsPost(
        data = NewsPostUiData(
            id = 0,
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            mediaList = persistentListOf(
                getMediaModel(),
                getMediaModel(),
                getMediaModel(),
            ),
            channelData = getChannelData(),
        ),
        loadMedia = ::loadMessageMedia,
        isStarred = true,
        onStarChannel = {},
        onLike = {},
        onDislike = {},
        markAsRead = {},
    )
}

private fun getMediaModel() = MediaModel(
    id = 2,
    height = 200,
    width = 200,
    sizeInMb = "200",
    type = MediaType.PHOTO,
)

private fun getChannelData() = ChannelUiData(
    id = 0,
    name = "Channel name",
    avatarPath = "",
    postTime = "2 min ago",
)

private suspend fun loadMessageMedia(mediaId: Int) = ""