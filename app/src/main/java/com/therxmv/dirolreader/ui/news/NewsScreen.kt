package com.therxmv.dirolreader.ui.news

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.therxmv.dirolreader.ui.commonview.CenteredBoxLoader
import com.therxmv.dirolreader.ui.news.view.NewsCollapsedToolbar
import com.therxmv.dirolreader.ui.news.viewmodel.NewsViewModel
import com.therxmv.dirolreader.ui.news.viewmodel.utils.NewsUiState
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
    val uiState by viewModel.uiState.collectAsState()
    val toolbarData by viewModel.toolbarDataState.collectAsState()
    val news = viewModel.news?.collectAsLazyPagingItems()

    val coroutineScope = rememberCoroutineScope()

    val newsFeedState = rememberLazyListState()
    val collapsingToolbarState = rememberCollapsingToolbarScaffoldState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        CollapsingToolbarScaffold(
            modifier = Modifier.padding(top = padding.calculateTopPadding()),
            state = collapsingToolbarState,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbar = {
                if (toolbarData.avatarPath.isNotEmpty()) {
                    NewsCollapsedToolbar(
                        toolbarState = collapsingToolbarState,
                        toolbarData = toolbarData,
                        newsFeedState = newsFeedState,
                        onAvatarClick = onNavigateToProfile,
                    )
                }
            },
        ) {
            if (news != null) {
                val starredChannelState = remember { mutableStateMapOf<Long, Boolean>() }
                val pullRefreshState = rememberPullRefreshState(
                    refreshing = uiState is NewsUiState.Loading,
                    onRefresh = {
                        viewModel.loadChannels {
                            coroutineScope.launch {
                                collapsingToolbarState.toolbarState.expand(500)
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
                        refreshing = uiState is NewsUiState.Loading,
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