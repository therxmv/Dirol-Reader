package com.therxmv.dirolreader.ui.news.view

import android.graphics.BitmapFactory
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.therxmv.common.R
import com.therxmv.dirolreader.ui.commonview.CenteredTopBar
import com.therxmv.dirolreader.ui.news.view.post.EmptyAvatar
import com.therxmv.dirolreader.ui.news.viewmodel.FeedViewModel
import com.therxmv.dirolreader.ui.news.viewmodel.utils.FeedUiState
import com.therxmv.dirolreader.ui.news.viewmodel.utils.ToolbarState
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    viewModel: FeedViewModel = hiltViewModel(),
    navController: NavController,
    onNavigateToProfile: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val uiState = viewModel.uiState.collectAsState().value
    val news = viewModel.news.collectAsLazyPagingItems()
    val starredChannels by viewModel.starredChannels.collectAsState()

    Scaffold(
        topBar = {
            if (uiState is FeedUiState.Ready) {
                NewsTopBar(
                    state = uiState.toolbarState,
                    navController = navController,
                    onAvatarClick = onNavigateToProfile,
                    scrollToTop = {
                        coroutineScope.launch {
                            listState.scrollToItem(0)
                        }
                    },
                )
            }
        },
        contentWindowInsets = WindowInsets(bottom = 0)
    ) { padding ->
        Crossfade(
            targetState = uiState,
            label = "content",
        ) {
            when (it) {
                is FeedUiState.Ready -> NewsScreenContent(
                    modifier = Modifier.padding(padding),
                    listState = listState,
                    news = news,
                    starredChannels = starredChannels.toPersistentList(),
                    onEvent = viewModel::onEvent,
                    loadMedia = viewModel::loadMessageMedia,
                )

                is FeedUiState.InitialState -> {} // Loader indicator is presented in pagination
            }
        }
    }
}

@Composable
private fun NewsTopBar(
    state: ToolbarState,
    navController: NavController,
    onAvatarClick: () -> Unit,
    scrollToTop: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    CenteredTopBar(
        title = {
            Column(
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = scrollToTop,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = state.userName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${state.unreadChannels} ${stringResource(id = R.string.news_unread_channels)}",
                )
            }
        },
        navController = navController,
        actions = {
            Avatar(
                state = state,
                onAvatarClick = onAvatarClick,
            )
        }
    )
}

@Composable
private fun Avatar(
    state: ToolbarState,
    onAvatarClick: () -> Unit,
) {
    if (state.avatarPath.isNotBlank()) {
        Image(
            bitmap = BitmapFactory.decodeFile(state.avatarPath).asImageBitmap(),
            contentDescription = "Avatar",
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clip(
                    MaterialTheme.shapes.small
                )
                .clickable(onClick = onAvatarClick)
        )
    } else {
        EmptyAvatar(
            modifier = Modifier
                .clickable(onClick = onAvatarClick),
            name = state.userName,
        )
    }
}