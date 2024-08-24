package com.therxmv.dirolreader.ui.news.view.post.media.types

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import com.therxmv.dirolreader.domain.models.MediaModel
import com.therxmv.dirolreader.ui.news.view.post.media.MediaPlaceholder

@Composable
fun PostPhoto(
    photoPath: String?,
    photo: MediaModel,
) {
    val imageRatio = photo.width.toFloat() / photo.height.toFloat()

    if (photoPath.isNullOrBlank()) {
        MediaPlaceholder(aspectRatio = imageRatio)
    } else {
        val bitmap = BitmapFactory.decodeFile(photoPath)

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "photo",
            modifier = Modifier
                .aspectRatio(imageRatio)
                .clip(MaterialTheme.shapes.small)
        )

        DisposableEffect(Unit) {
            onDispose {
                bitmap.recycle()
            }
        }
    }
}