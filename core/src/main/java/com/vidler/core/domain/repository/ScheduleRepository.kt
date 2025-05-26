package com.vidler.core.domain.repository

import com.vidler.core.domain.entity.Schedule
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun get(): Flow<List<Schedule>>
    suspend fun insert(schedule: Schedule)
    suspend fun delete(scheduleId: Int)
}