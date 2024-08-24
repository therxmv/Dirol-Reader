package com.therxmv.dirolreader.ui.news.view.post

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.therxmv.common.R

data class ChannelUiData(
    val id: Long,
    val name: String,
    val avatarPath: String?,
    val postTime: String,
)

@Composable
fun ChannelPostInfo(
    data: ChannelUiData,
    isStarred: Boolean,
    onStarChannel: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min),
    ) {
        if (data.avatarPath.isNullOrEmpty()) {
            EmptyAvatar(name = data.name)
        } else {
            Avatar(avatarPath = data.avatarPath)
        }
        Spacer(modifier = Modifier.width(8.dp))

        PostInfo(
            modifier = Modifier.weight(1f),
            channelName = data.name,
            postTime = data.postTime,
        )
        Spacer(modifier = Modifier.width(8.dp))

        StarChannel(
            isStarred = isStarred,
            onClick = onStarChannel,
        )
    }
}

@Composable
private fun StarChannel(
    isStarred: Boolean,
    onClick: (Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val icon = if (isStarred) R.drawable.star_filled_icon else R.drawable.star_outline_icon
    val tint = if (isStarred) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Icon(
        modifier = Modifier
            .size(24.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick(isStarred)
            },
        painter = painterResource(id = icon),
        contentDescription = "star",
        tint = tint,
    )
}

@Composable
private fun PostInfo(
    modifier: Modifier = Modifier,
    channelName: String,
    postTime: String,
) {
    Column(
        modifier = modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = channelName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
        Text(
            text = postTime,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
private fun Avatar(
    avatarPath: String,
) {
    val bitmap = BitmapFactory.decodeFile(avatarPath)

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "avatar",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(48.dp)
            .height(48.dp)
            .clip(MaterialTheme.shapes.small)
    )

    DisposableEffect(Unit) {
        onDispose {
            bitmap.recycle()
        }
    }
}

@Composable
fun EmptyAvatar(
    modifier: Modifier = Modifier,
    name: String,
) {
    val background = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier
            .width(48.dp)
            .height(48.dp)
            .clip(MaterialTheme.shapes.small)
            .drawBehind {
                drawRect(color = background)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.first().toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChannelPostInfoPreview() {
    ChannelPostInfo(
        data = ChannelUiData(
            id = 0,
            name = "Channel name",
            avatarPath = "",
            postTime = "2 min ago",
        ),
        isStarred = false,
        onStarChannel = {},
    )
}