package com.therxmv.dirolreader.ui.news

import android.graphics.BitmapFactory
import android.text.format.DateUtils
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.therxmv.dirolreader.R
import com.therxmv.dirolreader.data.models.MediaModel
import com.therxmv.dirolreader.data.models.MediaType
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.ui.news.posts.PostPhoto
import com.therxmv.dirolreader.ui.news.posts.PostVideo
import com.therxmv.dirolreader.ui.news.utils.NewsUiEvent
import com.therxmv.dirolreader.utils.MarkdownTextTemp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import kotlin.reflect.KSuspendFunction2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsPost(
    messageModel: MessageModel,
    starredState: MutableMap<Long, Boolean>,
    loadMedia: KSuspendFunction2<List<MediaModel>, Boolean, List<String?>>,
    onEvent: (event: NewsUiEvent) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val isLiked = rememberSaveable { mutableStateOf<Boolean?>(null) }
    val mediaPaths = rememberSaveable { mutableStateOf<List<String?>?>(null) }

    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
        ) {
            if(messageModel.mediaList != null) {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        if(mediaPaths.value == null) {
                            mediaPaths.value = loadMedia(
                                messageModel.mediaList,
                                false
                            )
                        }
                    }
                }

                val pagerState = rememberPagerState(initialPage = 0) {
                    messageModel.mediaList.size
                }

                if(messageModel.mediaList.size == 1) {
                    val item = messageModel.mediaList[0]

                    when(item.type) {
                        MediaType.PHOTO -> {
                            PostPhoto(
                                photoPath = mediaPaths.value?.get(0),
                                photo = item,
                            )
                        }
                        MediaType.VIDEO -> {
                            PostVideo(
                                0,
                                videoPath = mediaPaths.value?.get(0),
                                video = item,
                                mediaPaths,
                                loadMedia
                            )
                        }
                    }
                }
                else {
                    Box {
                        HorizontalPager(state = pagerState) { i ->
                            val item = messageModel.mediaList[i]

                            when(item.type) {
                                MediaType.PHOTO -> {
                                    PostPhoto(
                                        photoPath = mediaPaths.value?.get(i),
                                        photo = item,
                                    )
                                }
                                MediaType.VIDEO -> {
                                    PostVideo(
                                        i,
                                        videoPath = mediaPaths.value?.get(i),
                                        video = item,
                                        mediaPaths,
                                        loadMedia
                                    )
                                }
                            }
                        }

                        // MEDIA COUNTER
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
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
                                    text = "${pagerState.currentPage + 1} ${stringResource(id = R.string.news_counter_divider)} ${messageModel.mediaList.size}",
                                )
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                // CHANNEL INFO
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
                            fontWeight = FontWeight.Bold
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

                // TEXT
                if(messageModel.text.isNotEmpty()) {
                    val defaultColor = LocalContentColor.current
                    val linkColor = MaterialTheme.colorScheme.primary

                    MarkdownTextTemp(
                        modifier = Modifier
                            .padding(top = 12.dp),
                        markdown = messageModel.text,
                        color = defaultColor,
                        linkColor = linkColor,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                // LIKES
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(24.dp),
                        onClick = {
                            onEvent(NewsUiEvent.UpdateRating(
                                messageModel.channelId,
                                if (isLiked.value == null) 1 else if(isLiked.value == false) 2 else 0
                            ))
                            isLiked.value = true
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_up_icon),
                            contentDescription = "like",
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small),
                            tint = if(isLiked.value == null || isLiked.value == false) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary,
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .size(24.dp),
                        onClick = {
                            onEvent(NewsUiEvent.UpdateRating(
                                messageModel.channelId,
                                if (isLiked.value == null) -1 else if(isLiked.value == false) -2 else 0
                            ))
                            isLiked.value = false
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_down_icon),
                            contentDescription = "dislike",
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small),
                            tint = if(isLiked.value == null || isLiked.value == true) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
    }

//    onEvent(NewsUiEvent.MarkAsRead(messageModel.id, messageModel.channelId))
}

@Composable
private fun getPostTime(date: Int): String {
    val utcDate = Date(date * 1000L)
    val calendar = Calendar.getInstance().apply {
        timeZone = TimeZone.getDefault()
        time = utcDate
    }

    val postTime = calendar.timeInMillis
    val currentTime = System.currentTimeMillis()

    val diff = currentTime - postTime
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)

    return when {
        hours >= 24 -> {
            val dateFormat = SimpleDateFormat("dd.MM, HH:mm")
            dateFormat.timeZone = TimeZone.getDefault()

            dateFormat.format(Date(date * 1000L))
        }
        hours >= 1 -> {
            DateUtils.getRelativeTimeSpanString(postTime, currentTime, DateUtils.HOUR_IN_MILLIS).toString()
        }
        minutes >= 1 -> {
            DateUtils.getRelativeTimeSpanString(postTime, currentTime, DateUtils.MINUTE_IN_MILLIS).toString()
        }
        else -> {
            stringResource(R.string.news_just_now)
        }
    }
}