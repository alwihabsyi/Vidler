package com.vidler.core.domain.helper

import android.content.Context
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class DownloadHelper(
    private val context: Context, private val fileHelper: FileHelper
) {
    private var initialized = false

    init {
        initPrDownloader()
    }

    private fun initPrDownloader() {
        val config = PRDownloaderConfig.newBuilder().setDatabaseEnabled(true).build()
        PRDownloader.initialize(context, config)
        initialized = true
    }

    fun downloadFlow(name: String, contentUrl: String): Flow<DownloadResult> = callbackFlow {
        if (!initialized) initPrDownloader()
        val extension = contentUrl.substringAfterLast('.', "").substringBefore('?')
        val contentName = if (name.endsWith(".$extension")) name else "$name.$extension"

        val downloadId = PRDownloader.download(
            contentUrl, fileHelper.getAppDirectory().absolutePath, contentName
        ).build().setOnProgressListener { progress ->
            trySend(
                DownloadResult.Progress(
                    currentBytes = progress.currentBytes, totalBytes = progress.totalBytes
                )
            )
        }.start(object : OnDownloadListener {
            override fun onDownloadComplete() {
                trySend(DownloadResult.Complete)
                close()
            }

            override fun onError(error: Error?) {
                trySend(DownloadResult.Error(error?.serverErrorMessage ?: "Unknown error"))
                close()
            }
        })

        awaitClose {
            PRDownloader.cancel(downloadId)
        }
    }

    sealed class DownloadResult {
        data class Progress(val currentBytes: Long, val totalBytes: Long) : DownloadResult()
        data object Complete : DownloadResult()
        data class Error(val message: String) : DownloadResult()
    }
}