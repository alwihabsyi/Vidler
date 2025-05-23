package com.vidler.core.domain.helper

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import com.vidler.core.domain.entity.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

class FileHelper(private val context: Context) {

    private fun getAppDirectory(): File {
        val mediaDir = File(
            Environment.getExternalStorageDirectory(), "Android/media/${context.packageName}/media"
        )
        if (!mediaDir.exists()) mediaDir.mkdirs()
        return mediaDir
    }

    fun getVideoFilesFlow(pollingIntervalMs: Long = 1000L): Flow<List<Video>> = flow {
        var lastKnownFiles: List<File>? = null

        while (true) {
            val currentFiles = getAppDirectory().listFiles()?.toList() ?: emptyList()

            if (currentFiles != lastKnownFiles) {
                val videos =
                    currentFiles.filter { it.extension.lowercase() in listOf("mp4", "mkv", "avi") }
                        .map { file ->
                            val retriever = MediaMetadataRetriever()
                            var duration = 0L
                            var thumbnail: Bitmap? = null

                            try {
                                retriever.setDataSource(file.absolutePath)

                                duration =
                                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                                        ?.toLongOrNull() ?: 0L

                                thumbnail = retriever.getFrameAtTime(
                                    1_000_000, MediaMetadataRetriever.OPTION_CLOSEST
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            } finally {
                                retriever.release()
                            }

                            Video(
                                name = file.name,
                                path = file.absolutePath,
                                duration = duration,
                                thumbnail = thumbnail
                            )
                        }

                emit(videos)
                lastKnownFiles = currentFiles
            }
            delay(pollingIntervalMs)
        }
    }.flowOn(Dispatchers.IO)


    fun isFileExist(filePath: String): Boolean = File(filePath).exists()
}