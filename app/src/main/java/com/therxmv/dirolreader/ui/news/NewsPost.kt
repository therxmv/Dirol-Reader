package com.therxmv.dirolreader.ui.news

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.therxmv.dirolreader.R

@Composable
fun NewsPost() {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
        ) {
            val painter = painterResource(id = R.drawable.example_post_photo)
            val imageRatio = painter.intrinsicSize.width / painter.intrinsicSize.height
            Image(
                painter = painter,
                contentDescription = "photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(imageRatio)
                    .clip(MaterialTheme.shapes.small)
            )
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .height(IntrinsicSize.Min),
                ) {
                    Image(
                        painter = painter,
                        contentDescription = "photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(48.dp)
                            .height(48.dp)
                            .clip(MaterialTheme.shapes.small)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Channel name",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = "Post time ago",
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
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing consectetur consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
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
                        onClick = { /*TODO*/ },
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
                        onClick = { /*TODO*/ }
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

@Preview(showBackground = true)
@Composable
fun PreviewNewsPost() {
    NewsPost()
}