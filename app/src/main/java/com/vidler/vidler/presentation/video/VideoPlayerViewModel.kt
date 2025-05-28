package com.vidler.vidler.presentation.video

import androidx.lifecycle.ViewModel
import com.vidler.core.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.first

class VideoPlayerViewModel(
    private val scheduleRepository: ScheduleRepository
): ViewModel() {

    suspend fun getSchedule(id: String) =
        scheduleRepository.getById(id).first()
}