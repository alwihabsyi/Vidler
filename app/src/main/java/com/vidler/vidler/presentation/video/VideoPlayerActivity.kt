package com.vidler.vidler.presentation.video

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import com.vidler.core.domain.entity.Schedule
import com.vidler.vidler.MainActivity
import com.vidler.vidler.presentation.common.VideoPlayer
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import java.util.Date

class VideoPlayerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val videoUri = intent.getStringExtra("video_uri")
        val scheduleId = intent.getStringExtra("schedule_id")

        if (videoUri == null && scheduleId == null) return

        setContent {
            val viewModel = koinViewModel<VideoPlayerViewModel>()
            var schedule by remember { mutableStateOf<Schedule?>(null) }

            LaunchedEffect(scheduleId) {
                if (scheduleId != null) {
                    schedule = viewModel.getSchedule(scheduleId)
                }
            }

            LaunchedEffect(schedule?.endTime) {
                schedule?.endTime?.let { endTime ->
                    while (true) {
                        if (Date() >= endTime) {
                            startActivity(Intent(this@VideoPlayerActivity, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            })
                            break
                        }
                        delay(1000)
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                when {
                    videoUri != null -> {
                        Log.i("INI VIDEOS", "vid uri trigger rawr")
                        VideoPlayer(videoUri = videoUri.toUri()) { finish() }
                    }

                    schedule != null -> {
                        VideoPlayer(videosUri = schedule!!.videos.map { it.path.toUri() }) { finish() }
                    }

                    else -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center), color = Color.White
                        )
                    }
                }
            }
        }

    }
}