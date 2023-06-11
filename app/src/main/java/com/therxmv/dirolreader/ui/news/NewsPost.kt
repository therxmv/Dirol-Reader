package com.therxmv.dirolreader.ui.news

import android.graphics.BitmapFactory
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

@Composable
fun NewsPost(
    messageModel: MessageModel
) {
    // TODO mark as read
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
                    Icon(
                        painter = painterResource(id = R.drawable.star_outline_icon),
                        contentDescription = "star",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null
                            ) {
                                // TODO add to favorite
                            },
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
                    IconButton(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(24.dp),
                        onClick = {
                            // TODO like
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_up_icon),
                            contentDescription = "like",
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small),
                        )
                    }
                    IconButton(
                        modifier = Modifier
                            .size(24.dp),
                        onClick = {
                            // TODO dislike
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_down_icon),
                            contentDescription = "dislike",
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.small),
                        )
                    }
                }
            }
        }
    }
}

private fun getPostTime(date: Int): String {
    val dateFormat = SimpleDateFormat("dd.MM, HH:mm")
    dateFormat.timeZone = TimeZone.getDefault()

    return dateFormat.format(Date(date * 1000L))
    // TODO maybe add "today", "yesterday"
}

@Preview(showBackground = true)
@Composable
fun PreviewNewsPost() {
    NewsPost(
        MessageModel(
            0,
            0,
            0,
            "Channel Name",
            null,
            1856546,
            "Lorem ipsum dolor sit amet, consectetur adipiscing consectetur consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        null
        )
    )
}