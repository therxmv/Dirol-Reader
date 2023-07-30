package com.therxmv.dirolreader.ui.news.posts

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import com.therxmv.dirolreader.data.models.MediaModel

@Composable
fun PostPhoto(
    photoPath: String?,
    photo: MediaModel,
) {
    if(photoPath.isNullOrBlank()) {
        val imageRatio = photo.width.toFloat() / photo.height.toFloat()
        Box(
            modifier = Modifier
                .aspectRatio(imageRatio)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.secondary)
        )
    }
    else {
        val bitmap = BitmapFactory.decodeFile(photoPath).asImageBitmap()
        val imageRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

        Image(
            bitmap = bitmap,
            contentDescription = "photo",
            modifier = Modifier
                .aspectRatio(imageRatio)
                .clip(MaterialTheme.shapes.small)
        )
    }
}