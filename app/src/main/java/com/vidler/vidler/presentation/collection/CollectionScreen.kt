package com.vidler.vidler.presentation.collection

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vidler.core.domain.helper.DownloadHelper
import com.vidler.vidler.presentation.collection.components.CollectionItem
import com.vidler.vidler.presentation.collection.components.FileInputDialog
import com.vidler.vidler.presentation.video.VideoPlayerActivity
import com.vidler.vidler.util.SnackBarManager

@Composable
fun CollectionScreen(
    modifier: Modifier = Modifier, state: CollectionState, onEvent: (CollectionEvent) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.videos.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "No videos yet",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Column {
                Text(
                    "Video Collection",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
                )
                LazyColumn(
                    contentPadding = PaddingValues(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.videos) { video ->
                        CollectionItem(modifier = modifier.padding(horizontal = 16.dp), videoItem = video) {
                            val intent = Intent(context, VideoPlayerActivity::class.java).apply {
                                putExtra("video_uri", video.path)
                            }
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            FloatingActionButton(
                modifier = Modifier.size(48.dp),
                onClick = {
                    showDialog = true
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        FileInputDialog(
            showDialog = showDialog,
            onStoragePic = { onEvent(CollectionEvent.OnStorageMove(it)) },
            onUrl = { name, url -> onEvent(CollectionEvent.OnDownload(name, url)) },
            onDismiss = { showDialog = false })

        when (state.downloadProgress) {
            is DownloadHelper.DownloadResult.Progress -> {
                val progress = state.downloadProgress
                val percentage = if (progress.totalBytes > 0)
                    (progress.currentBytes * 100 / progress.totalBytes).toInt()
                else 0
                SnackBarManager.show(
                    "Downloading Content",
                    "$percentage%"
                )
            }

            else -> {
                SnackBarManager.dismiss()
            }
        }
    }
}