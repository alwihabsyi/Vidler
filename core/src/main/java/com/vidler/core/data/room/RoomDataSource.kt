package com.vidler.core.data.room

import com.vidler.core.data.room.dao.ScheduleDao
import com.vidler.core.domain.entity.Schedule

class RoomDataSource(
    private val scheduleDao: ScheduleDao
) {

    fun getSchedule() = scheduleDao.getSchedules()
    suspend fun insertSchedule(schedule: Schedule) = scheduleDao.insertSchedule(schedule)
    suspend fun deleteSchedule(scheduleId: Int) = scheduleDao.deleteSchedule(scheduleId)
}