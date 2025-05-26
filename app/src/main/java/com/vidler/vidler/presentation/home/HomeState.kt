package com.vidler.vidler.presentation.home

import com.vidler.core.domain.entity.Schedule
import com.vidler.core.domain.entity.Video

data class HomeState(
    val videos: List<Video> = emptyList(),
    val nearestSchedule: List<Schedule> = emptyList(),
    val latestSchedule: List<Schedule> = emptyList()
)