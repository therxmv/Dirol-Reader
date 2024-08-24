package com.therxmv.dirolreader.ui.news.view.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.therxmv.common.R
import com.therxmv.dirolreader.domain.models.MediaModel
import com.therxmv.dirolreader.ui.news.view.post.media.MediaLoaderType
import com.therxmv.dirolreader.ui.news.view.post.media.PostMediaContent
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.collections.immutable.PersistentList

data class NewsPostUiData(
    val id: Long,
    val text: String,
    val mediaList: PersistentList<MediaModel>?,
    val channelData: ChannelUiData,
)

@Composable
fun NewsPost(
    data: NewsPostUiData,
    loadMedia: MediaLoaderType,
    isStarred: Boolean,
    onStarChannel: (Boolean) -> Unit,
    onLike: (Boolean?) -> Unit,
    onDislike: (Boolean?) -> Unit,
    markAsRead: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
        ) {
            data.mediaList?.let {
                PostMediaContent(
                    mediaList = it,
                    loadMedia = loadMedia,
                )
            }

            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                ChannelPostInfo(
                    data = data.channelData,
                    isStarred = isStarred,
                    onStarChannel = onStarChannel,
                )

                if (data.text.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    PostText(text = data.text)
                }
                Spacer(modifier = Modifier.height(12.dp))

                PostLikes(
                    onLike = onLike,
                    onDislike = onDislike,
                )
            }
        }
    }

    SideEffect {
        markAsRead()
    }
}

@Composable
private fun PostLikes(
    onLike: (Boolean?) -> Unit,
    onDislike: (Boolean?) -> Unit,
) {
    var isLiked by rememberSaveable { mutableStateOf<Boolean?>(null) }

    val pressedColor = MaterialTheme.colorScheme.primary
    val defaultColor = MaterialTheme.colorScheme.onSurfaceVariant
    val iconTint: @Composable (Boolean) -> Color = remember {
        {
            if (isLiked == it) pressedColor else defaultColor
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        IconButton(
            modifier = Modifier
                .size(24.dp),
            onClick = {
                onLike(isLiked)
                isLiked = if (isLiked == true) null else true
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_up_icon),
                contentDescription = "like",
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small),
                tint = iconTint(true),
            )
        }
        Spacer(modifier = Modifier.width(16.dp))

        IconButton(
            modifier = Modifier
                .size(24.dp),
            onClick = {
                onDislike(isLiked)
                isLiked = if (isLiked == false) null else false
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_down_icon),
                contentDescription = "dislike",
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small),
                tint = iconTint(false),
            )
        }
    }
}

@Composable
private fun PostText(
    text: String,
) {
    MarkdownText( // TODO 1 might be the issue of ANR
        markdown = text,
        linkColor = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.bodyLarge,
        isTextSelectable = true
    )
}