package com.vidler.core.domain.helper

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.vidler.core.domain.entity.Video
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import java.io.FileOutputStream

class FileHelper(private val context: Context) {

    private fun getAppDirectory(): File {
        val mediaDir = File(
            Environment.getExternalStorageDirectory(), "Android/media/${context.packageName}/media"
        )
        if (!mediaDir.exists()) {
            val wasCreated = mediaDir.mkdirs()
            if (!wasCreated) {
                Log.e("FileHelper", "Failed to create media directory: ${mediaDir.absolutePath}")
            }
        }
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

    fun moveVideosToAppDirectory(uris: List<Uri>): List<File> {
        val movedFiles = mutableListOf<File>()
        val contentResolver = context.contentResolver

        for (uri in uris) {
            try {
                val fileName = getFileNameFromUri(uri, contentResolver) ?: continue
                val destFile = File(getAppDirectory(), fileName)
                if (destFile.exists()) continue

                contentResolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(destFile).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }

                movedFiles.add(destFile)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return movedFiles
    }

    private fun getFileNameFromUri(uri: Uri, contentResolver: ContentResolver): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        }
    }

}