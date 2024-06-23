package com.therxmv.dirolreader.ui.news.view

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.therxmv.common.R
import com.therxmv.dirolreader.ui.news.viewmodel.utils.ToolbarData
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffoldState
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.ExperimentalToolbarApi

@OptIn(ExperimentalToolbarApi::class)
@Composable
fun CollapsingToolbarScope.NewsCollapsedToolbar(
    toolbarState: CollapsingToolbarScaffoldState,
    toolbarData: ToolbarData,
    newsFeedState: LazyListState,
    onAvatarClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

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
            text = toolbarData.userName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${toolbarData.unreadChannels} ${stringResource(id = R.string.news_unread_channels)}",
        )
    }

    Image(
        bitmap = BitmapFactory.decodeFile(toolbarData.avatarPath).asImageBitmap(),
        contentDescription = "Avatar",
        modifier = Modifier
            .padding(8.dp)
            .width(48.dp)
            .height(48.dp)
            .clip(
                MaterialTheme.shapes.small
            )
            .clickable {
                onAvatarClick()
            }
            .road(Alignment.CenterEnd, Alignment.TopCenter)
    )

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            toolbarState.toolbarState.expand(1000)
        }
    }
}