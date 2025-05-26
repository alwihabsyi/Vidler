package com.vidler.vidler.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidler.core.domain.helper.FileHelper
import com.vidler.core.domain.repository.ScheduleRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val fileHelper: FileHelper,
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {
    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    init {
        observeVideos()
        observeSchedule()
    }

    private fun observeVideos() = viewModelScope.launch {
        fileHelper.getVideoFilesFlow().collect {
            _state.value = _state.value.copy(videos = it)
        }
    }

    private fun observeSchedule() = viewModelScope.launch {
        scheduleRepository.get().collect { schedules ->
            _state.value =
                _state.value.copy(
                    nearestSchedule = schedules.sortedBy { it.startTime },
                    latestSchedule = schedules.sortedByDescending { it.createdAt }
                )
        }
    }
}