package com.therxmv.dirolreader.ui.news

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.therxmv.common.R
import com.therxmv.dirolreader.ui.commonview.CenteredBoxLoader
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@OptIn(
    ExperimentalToolbarApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun NewsScreen(
    viewModel: NewsViewModel = hiltViewModel(),
    onNavigateToProfile: () -> Unit,
) {
    val state = viewModel.state.collectAsState().value
    val news = viewModel.news?.collectAsLazyPagingItems()

    val coroutineScope = rememberCoroutineScope()

    val newsFeedState = rememberLazyListState()
    val toolbarState = rememberCollapsingToolbarScaffoldState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        CollapsingToolbarScaffold(
            modifier = Modifier.padding(top = padding.calculateTopPadding()),
            state = toolbarState,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {
                if (state.toolbarState.avatarPath.isNotBlank()) with(state.toolbarState) {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                            .height(125.dp)
                            .pin()
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                coroutineScope.launch {
                                    toolbarState.toolbarState.expand(500)
                                }
                                coroutineScope.launch {
                                    newsFeedState.scrollToItem(0)
                                }
                            }
                            .road(Alignment.Center, Alignment.BottomCenter),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$unreadChannels ${stringResource(id = R.string.news_unread_channels)}",
                        )
                    }
                    Image(
                        bitmap = BitmapFactory.decodeFile(avatarPath).asImageBitmap(),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .padding(8.dp)
                            .width(48.dp)
                            .height(48.dp)
                            .clip(
                                MaterialTheme.shapes.small
                            )
                            .clickable {
                                onNavigateToProfile()
                            }
                            .road(Alignment.CenterEnd, Alignment.TopCenter)
                    )
                    LaunchedEffect(Unit) {
                        coroutineScope.launch {
                            toolbarState.toolbarState.expand(1000)
                        }
                    }
                }
            },
        ) {
            if (news != null) {
                val starredChannelState = remember { mutableStateMapOf<Long, Boolean>() }
                val pullRefreshState = rememberPullRefreshState(
                    refreshing = state.isLoaded.not(),
                    onRefresh = {
                        viewModel.loadChannels {
                            coroutineScope.launch {
                                toolbarState.toolbarState.expand(500)
                                newsFeedState.scrollToItem(0, 0)
                            }
                            news.refresh()
                        }
                    }
                )

                Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        state = newsFeedState,
                    ) {
                        items(
                            count = news.itemCount,
                            key = news.itemKey(key = { it.id }),
                            contentType = news.itemContentType()
                        ) { index ->
                            val item = news[index]!!

                            NewsPost(
                                item,
                                starredChannelState,
                                viewModel::loadMessageMedia,
                                viewModel::onEvent
                            )
                        }
                    }
                    PullRefreshIndicator(
                        refreshing = state.isLoaded.not(),
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            } else {
                CenteredBoxLoader()
            }
        }
    }
}