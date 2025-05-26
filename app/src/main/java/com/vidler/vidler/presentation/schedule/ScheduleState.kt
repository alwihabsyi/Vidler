package com.vidler.vidler.presentation.schedule

import com.vidler.core.domain.entity.Schedule

data class ScheduleState(
    val schedule: List<Schedule> = emptyList()
)