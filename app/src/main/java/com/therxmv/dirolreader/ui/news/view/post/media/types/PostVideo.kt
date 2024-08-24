package com.therxmv.dirolreader.ui.news.view.post.media.types

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.therxmv.dirolreader.R
import com.therxmv.dirolreader.domain.models.MediaModel
import com.therxmv.dirolreader.ui.news.view.post.media.MediaPlaceholder
import com.therxmv.common.R as CommonR

@Composable
fun PostVideo(
    videoPath: String?,
    video: MediaModel,
    downloadMedia: () -> Unit,
) {
    val videoRatio = video.width.toFloat() / video.height.toFloat()
    if (videoPath.isNullOrBlank()) {
        Placeholder(
            video = video,
            aspectRatio = videoRatio,
            onDownloadClick = downloadMedia,
        )
    } else {
        Player(
            modifier = Modifier.aspectRatio(videoRatio),
            videoPath = videoPath,
        )
    }
}

@Composable
private fun Player(
    modifier: Modifier = Modifier,
    videoPath: String,
) {
    val context = LocalContext.current
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    exoPlayer.apply {
        setMediaItem(MediaItem.fromUri(videoPath))
        prepare()
    }

    val playerView = remember {
        val layout = LayoutInflater.from(context).inflate(R.layout.video_player, null, false)
        val playerView: PlayerView = layout.findViewById(R.id.playerView)

        handlePlayPauseButtons(playerView, exoPlayer)

        playerView.apply {
            player = exoPlayer
        }
    }

    AndroidView(
        factory = { playerView },
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
    )

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }
}

@Composable
private fun Placeholder(
    video: MediaModel,
    aspectRatio: Float,
    onDownloadClick: () -> Unit,
) {
    var isDownloading by remember { mutableStateOf(false) }

    MediaPlaceholder(aspectRatio = aspectRatio) {
        if (isDownloading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onSecondary,
            )
        } else {
            Column(
                modifier = Modifier
                    .clickable {
                        isDownloading = true
                        onDownloadClick()
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    painter = painterResource(id = CommonR.drawable.download_icon),
                    contentDescription = "download",
                    tint = MaterialTheme.colorScheme.onSecondary,
                )
                Text(
                    text = "${video.sizeInMb} MB",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
private fun handlePlayPauseButtons(playerView: PlayerView, exoPlayer: ExoPlayer) {
    val playButton = playerView.findViewById<ImageButton>(R.id.playButton)
    val pauseButton = playerView.findViewById<ImageButton>(R.id.pauseButton)

    playButton.setOnClickListener {
        if (playerView.player?.playbackState == Player.STATE_ENDED) {
            playerView.player?.seekTo(0L)
        }

        it.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
        playerView.player?.play()
        playerView.hideController()
    }

    pauseButton.setOnClickListener {
        it.visibility = View.GONE
        playButton.visibility = View.VISIBLE
        playerView.player?.pause()
    }

    exoPlayer.addListener(object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            if (state == Player.STATE_ENDED) {
                pauseButton.visibility = View.GONE
                playButton.visibility = View.VISIBLE
            }
        }
    })
}