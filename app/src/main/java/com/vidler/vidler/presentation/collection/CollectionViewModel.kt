package com.vidler.vidler.presentation.collection

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidler.core.domain.helper.FileHelper
import kotlinx.coroutines.launch

class CollectionViewModel(private val fileHelper: FileHelper) : ViewModel() {
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
}