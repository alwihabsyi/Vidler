package com.vidler.core.data.repository

import com.vidler.core.data.room.RoomDataSource
import com.vidler.core.domain.entity.Schedule
import com.vidler.core.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow

class ScheduleRepositoryImpl(private val roomDataSource: RoomDataSource): ScheduleRepository {
    override fun get(): Flow<List<Schedule>> =
        roomDataSource.getSchedule()

    override suspend fun insert(schedule: Schedule) =
        roomDataSource.insertSchedule(schedule)

    override suspend fun delete(scheduleId: Int) =
        roomDataSource.deleteSchedule(scheduleId)
}