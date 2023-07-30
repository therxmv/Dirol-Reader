package com.therxmv.dirolreader.ui.news.posts

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.therxmv.dirolreader.R
import com.therxmv.dirolreader.data.models.MediaModel
import kotlinx.coroutines.launch
import java.math.RoundingMode
import kotlin.reflect.KSuspendFunction2

@Composable
fun PostVideo(
    pos: Int,
    videoPath: String?,
    video: MediaModel,
    mediaState: MutableState<List<String?>?>,
    loadMedia: KSuspendFunction2<List<MediaModel>, Boolean, List<String?>>,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    if(videoPath.isNullOrBlank()) {
        val isDownloading = remember { mutableStateOf(false) }
        val videoRatio = video.width.toFloat() / video.height.toFloat()

        Box(
            modifier = Modifier
                .aspectRatio(videoRatio)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.secondary),
            contentAlignment = Alignment.Center
        ) {
            if(isDownloading.value) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
            else {
                Column(
                    modifier = Modifier
                        .clickable {
                            isDownloading.value = true
                            coroutineScope.launch {
                                val temp = mediaState.value?.toMutableList() ?: mutableListOf()
                                temp[pos] = loadMedia(listOf(video), true).first()
                                mediaState.value = temp
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = R.drawable.download_icon),
                        contentDescription = "download",
                        tint = MaterialTheme.colorScheme.onSecondary,
                    )
                    Text(
                        text = "${(video.size / 1048576F).toBigDecimal().setScale(1, RoundingMode.UP)} MB",
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
    else {
        val videoRatio = video.width.toFloat() / video.height.toFloat()

        exoPlayer.apply {
            setMediaItem(MediaItem.fromUri(videoPath))
            prepare()
        }

        val playerView = remember {
            val layout = LayoutInflater.from(context).inflate(R.layout.video_player, null, false)
            val playerView = layout.findViewById(R.id.playerView) as PlayerView

            handlePlayPauseButtons(playerView, exoPlayer)

            playerView.apply {
                player = exoPlayer
            }
        }

        AndroidView(
            factory = { playerView },
            modifier = Modifier
                .aspectRatio(videoRatio)
                .clip(MaterialTheme.shapes.small)
        )
    }
}

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
private fun handlePlayPauseButtons(playerView: PlayerView, exoPlayer: ExoPlayer) {
    val playBtn = playerView.findViewById<ImageButton>(R.id.playButton)
    val pauseBtn = playerView.findViewById<ImageButton>(R.id.pauseButton)

    playBtn.setOnClickListener {
        if(playerView.player?.playbackState == Player.STATE_ENDED) {
            playerView.player?.seekTo(0L)
        }

        it.visibility = View.GONE
        pauseBtn.visibility = View.VISIBLE
        playerView.player?.play()
        playerView.hideController()
    }

    pauseBtn.setOnClickListener {
        it.visibility = View.GONE
        playBtn.visibility = View.VISIBLE
        playerView.player?.pause()
    }

    exoPlayer.addListener(object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            if (state == Player.STATE_ENDED) {
                pauseBtn.visibility = View.GONE
                playBtn.visibility = View.VISIBLE
            }
        }
    })
}