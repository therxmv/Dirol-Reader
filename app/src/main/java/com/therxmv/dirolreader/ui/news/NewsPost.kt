package com.therxmv.dirolreader.ui.news

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.therxmv.dirolreader.R
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.ui.news.utils.NewsUiEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

@Composable
fun NewsPost(
    messageModel: MessageModel,
    likedState: MutableMap<Long, Boolean?>,
    starredState: MutableMap<Long, Boolean>,
    readState: MutableMap<Long, Boolean>,
    onEvent: (event: NewsUiEvent) -> Unit,
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
            if(messageModel.photoPath != null) {
                val bitmap = BitmapFactory.decodeFile(messageModel.photoPath).asImageBitmap()
                val imageRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

                Image(
                    bitmap = bitmap,
                    contentDescription = "photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(imageRatio)
                        .clip(MaterialTheme.shapes.small)
                )
            }
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min),
                ) {
                    if (messageModel.channelAvatarPath.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = messageModel.channelName[0].toString(),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    else {
                        Image(
                            bitmap = BitmapFactory.decodeFile(messageModel.channelAvatarPath).asImageBitmap(),
                            contentDescription = "avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                                .clip(MaterialTheme.shapes.small)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = messageModel.channelName,
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = getPostTime(messageModel.timestamp),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                    val isStarred = starredState[messageModel.channelId] ?: (messageModel.channelRating >= 100)

                    Icon(
                        painter = if(isStarred) painterResource(id = R.drawable.star_filled_icon) else painterResource(id = R.drawable.star_outline_icon),
                        contentDescription = "star",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                onEvent(
                                    NewsUiEvent.UpdateRating(
                                        messageModel.channelId,
                                        if (isStarred) -100 else 100
                                    )
                                )

                                starredState[messageModel.channelId] = !isStarred
                            },
                        tint = if(isStarred) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = messageModel.text,
                    modifier = Modifier
                        .padding(top = 12.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    val isLiked = likedState[messageModel.id]

                    IconButton(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(24.dp),
                        onClick = {
                            onEvent(NewsUiEvent.UpdateRating(
                                messageModel.channelId,
                                if (isLiked == null) 1 else if(isLiked == false) 2 else 0
                            ))
                            likedState[messageModel.id] = true
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_up_icon),
                            contentDescription = "like",
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small),
                            tint = if(isLiked == null || isLiked == false) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary,
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .size(24.dp),
                        onClick = {
                            onEvent(NewsUiEvent.UpdateRating(
                                messageModel.channelId,
                                if (isLiked == null) -1 else if(isLiked == false) -2 else 0
                            ))
                            likedState[messageModel.id] = false
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_down_icon),
                            contentDescription = "dislike",
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small),
                            tint = if(isLiked == null || isLiked == true) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
    }

    val isRead = readState[messageModel.id] ?: false

    if(!isRead) {
        Log.d("rozmi", "${messageModel.text} read")
        onEvent(NewsUiEvent.MarkAsRead(messageModel.id, messageModel.channelId))
        readState[messageModel.id] = true
    }
}

private fun getPostTime(date: Int): String {
    val dateFormat = SimpleDateFormat("dd.MM, HH:mm")
    dateFormat.timeZone = TimeZone.getDefault()

    return dateFormat.format(Date(date * 1000L))
    // TODO maybe add "today", "yesterday"
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewNewsPost() {
//    NewsPost(
//        MessageModel(
//            0,
//            0,
//            0,
//            "Channel Name",
//            null,
//            1856546,
//            "Lorem ipsum dolor sit amet, consectetur adipiscing consectetur consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
//        null
//        ),
//        null,
//        { },
//    )
//}