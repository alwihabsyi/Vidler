package com.vidler.vidler.presentation.schedule

import com.vidler.core.domain.entity.Schedule

sealed class ScheduleEvent {
    data class OnAddSchedule(val schedule: Schedule): ScheduleEvent()
    data class OnDeleteSchedule(val scheduleId: String): ScheduleEvent()
}