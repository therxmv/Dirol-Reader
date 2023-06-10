package com.therxmv.dirolreader.ui.news

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.therxmv.dirolreader.R
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbar
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import me.onebone.toolbar.rememberCollapsingToolbarState

@OptIn(ExperimentalToolbarApi::class)
@Composable
fun NewsScreen(
    viewModel: NewsViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    val coroutineScope = rememberCoroutineScope()

    val newsFeedState = rememberLazyListState()
    val toolbarState = rememberCollapsingToolbarScaffoldState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        CollapsingToolbarScaffold(
            modifier = Modifier,
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
                                    newsFeedState.animateScrollToItem(0)
                                }
                                coroutineScope.launch {
                                    toolbarState.toolbarState.expand(500)
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
                                // TODO open user profile with channels
                            }
                            .road(Alignment.CenterEnd, Alignment.TopCenter)
                    )
                }
            },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = newsFeedState
            ) {
                coroutineScope.launch {
                    toolbarState.toolbarState.expand(1000)
                }
                items(10) {
                    NewsPost()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ToolbarPreview() {
    Scaffold(
        topBar = {
            CollapsingToolbar(collapsingToolbarState = rememberCollapsingToolbarState()) {
                if (true) {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                            .height(125.dp)
                            .pin()
                    )

//                Image(
//                    bitmap = BitmapFactory.decodeFile(avatarPath).asImageBitmap(),
//                    contentDescription = "Avatar",
//                    modifier = Modifier.road(Alignment.CenterEnd, Alignment.Center)
//                )
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(8.dp)
                            .width(48.dp)
                            .height(48.dp)
                            .road(Alignment.CenterEnd, Alignment.TopCenter)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .road(Alignment.Center, Alignment.BottomCenter),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "therxmv",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "5 unread channels",
                        )
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {
            items(100) {
                Text(
                    text = "Item $it",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}