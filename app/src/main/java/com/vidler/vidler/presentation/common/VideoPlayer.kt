package com.vidler.vidler.presentation.common

import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.WindowManager
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.vidler.vidler.util.findActivity

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    videoUri: Uri? = null,
    videosUri: List<Uri>? = null,
    onVideoEnded: () -> Unit
) {
    val context = LocalContext.current
    val activity = context.findActivity()

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            videoUri?.let { setMediaItem(MediaItem.fromUri(videoUri)) }
            videosUri?.let { setMediaItems(videosUri.map { MediaItem.fromUri(it) }) }
            repeatMode = if (videosUri != null) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
            prepare()
            play()
        }
    }

    LaunchedEffect(videosUri) {
        if (videosUri != null) {
            activity?.apply {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    DisposableEffect(key1 = exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == ExoPlayer.STATE_ENDED) {
                    if (videosUri == null)
                        onVideoEnded()
                }
            }
        }
        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
            if (videosUri != null) {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                useController = videosUri == null
            }
        }, modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    )
}