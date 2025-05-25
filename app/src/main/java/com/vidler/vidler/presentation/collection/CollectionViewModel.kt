package com.vidler.vidler.presentation.collection

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidler.core.domain.helper.DownloadHelper
import com.vidler.core.domain.helper.DownloadHelper.DownloadResult
import com.vidler.core.domain.helper.FileHelper
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val fileHelper: FileHelper, private val downloadHelper: DownloadHelper
) : ViewModel() {
    private val _state = mutableStateOf(CollectionState())
    val state: State<CollectionState> = _state

    init {
        observeVideoCollection()
    }

    private fun observeVideoCollection() = viewModelScope.launch {
        fileHelper.getVideoFilesFlow().collect {
            _state.value = _state.value.copy(videos = it)
        }
    }

    fun onEvent(event: CollectionEvent): Any = when (event) {
        is CollectionEvent.OnStorageMove -> {
            fileHelper.moveVideosToAppDirectory(event.uris)
        }

        is CollectionEvent.OnDownload -> {
            downloadFile(event.name, event.url)
        }
    }

    private fun downloadFile(name: String, url: String) = viewModelScope.launch {
        downloadHelper.downloadFlow(name, url).collect {
            _state.value =
                _state.value.copy(downloadProgress = it.takeIf { it !is DownloadResult.Complete })
        }
    }
}