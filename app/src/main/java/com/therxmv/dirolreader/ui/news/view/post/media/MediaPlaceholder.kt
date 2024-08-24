package com.therxmv.dirolreader.ui.news.view.post.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind

@Composable
fun MediaPlaceholder(
     aspectRatio: Float,
     content: @Composable (BoxScope.() -> Unit) = {},
) {
    val background = MaterialTheme.colorScheme.secondary
    Box(
        modifier = Modifier
            .aspectRatio(aspectRatio)
            .clip(MaterialTheme.shapes.small)
            .drawBehind {
                drawRect(color = background)
            },
        contentAlignment = Alignment.Center,
        content = content,
    )
}