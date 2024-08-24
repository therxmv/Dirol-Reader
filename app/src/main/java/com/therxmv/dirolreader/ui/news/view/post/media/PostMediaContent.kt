package com.therxmv.dirolreader.ui.news.view.post.media

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.therxmv.common.R
import com.therxmv.dirolreader.domain.models.MediaModel
import com.therxmv.dirolreader.domain.models.MediaType
import com.therxmv.dirolreader.ui.news.view.post.media.types.PostPhoto
import com.therxmv.dirolreader.ui.news.view.post.media.types.PostVideo
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.launch

typealias MediaLoaderType = suspend (Int) -> String?

@Composable
fun PostMediaContent(
    mediaList: PersistentList<MediaModel>,
    loadMedia: MediaLoaderType,
) {
    when {
        mediaList.size == 1 -> MediaByType(
            media = mediaList.first(),
            loadMedia = loadMedia,
        )

        else -> MediaPager(
            mediaList = mediaList,
            loadMedia = loadMedia,
        )
    }
}

@Composable
private fun MediaByType(
    media: MediaModel,
    loadMedia: MediaLoaderType,
) {
    val coroutineScope = rememberCoroutineScope()
    var downloadedMedia by rememberSaveable { mutableStateOf<String?>(null) }

    when (media.type) {
        MediaType.PHOTO -> {
            PostPhoto(
                photoPath = downloadedMedia,
                photo = media,
            )
            LaunchedEffect(Unit) {
                if (downloadedMedia == null) {
                    coroutineScope.launch {
                        downloadedMedia = loadMedia(media.id)
                    }
                }
            }
        }

        MediaType.VIDEO -> {
            PostVideo(
                videoPath = downloadedMedia,
                video = media,
                downloadMedia = {
                    coroutineScope.launch {
                        downloadedMedia = loadMedia(media.id)
                    }
                },
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaPager(
    mediaList: PersistentList<MediaModel>,
    loadMedia: MediaLoaderType,
) {
    val pagerState = rememberPagerState(initialPage = 0) { mediaList.size }

    Box(
        modifier = Modifier
            .animateContentSize(tween(200))
    ) {
        // TODO 2 need some avg value and open fullscreen
        HorizontalPager(state = pagerState) { i ->
            MediaByType(
                media = mediaList[i],
                loadMedia = loadMedia,
            )
        }

        MediaPagerCounter(
            modifier = Modifier.align(Alignment.TopCenter),
            currentPage = pagerState.currentPage + 1,
            allPages = mediaList.size,
        )
    }
}

@Composable
private fun MediaPagerCounter(
    modifier: Modifier = Modifier,
    currentPage: Int,
    allPages: Int,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
    ) {
        Card(
            shape = MaterialTheme.shapes.small,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
            )
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 3.dp, horizontal = 8.dp),
                text = "$currentPage ${stringResource(id = R.string.news_counter_divider)} $allPages",
            )
        }
    }
}

@Preview
@Composable
private fun MediaPagerCounterPreview() {
    MediaPagerCounter(currentPage = 1, allPages = 3)
}