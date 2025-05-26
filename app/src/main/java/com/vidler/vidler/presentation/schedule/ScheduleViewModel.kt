package com.vidler.vidler.presentation.schedule

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidler.core.domain.entity.Schedule
import com.vidler.core.domain.repository.ScheduleRepository
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {
    private val _state = mutableStateOf(ScheduleState())
    val state: State<ScheduleState> = _state

    init {
        observeSchedule()
    }

    private fun observeSchedule() = viewModelScope.launch {
        scheduleRepository.get().collect {
            _state.value = _state.value.copy(schedule = it)
        }
    }

    fun onEvent(event: ScheduleEvent): Any = when(event) {
        is ScheduleEvent.OnAddSchedule -> {
            insertSchedule(event.schedule)
        }

        is ScheduleEvent.OnDeleteSchedule -> {
            deleteSchedule(event.scheduleId)
        }
    }

    private fun deleteSchedule(scheduleId: Int) = viewModelScope.launch {
        scheduleRepository.delete(scheduleId)
    }

    private fun insertSchedule(schedule: Schedule) = viewModelScope.launch {
        scheduleRepository.insert(schedule)
    }
}