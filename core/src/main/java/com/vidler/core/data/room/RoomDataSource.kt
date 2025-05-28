package com.vidler.core.data.room

import com.vidler.core.data.room.dao.ScheduleDao
import com.vidler.core.domain.entity.Schedule

class RoomDataSource(
    private val scheduleDao: ScheduleDao
) {

    fun getSchedule() = scheduleDao.getSchedules()
    fun getScheduleById(id: String) = scheduleDao.getScheduleById(id)
    suspend fun insertSchedule(schedule: Schedule) = scheduleDao.insertSchedule(schedule)
    suspend fun deleteSchedule(scheduleId: String) = scheduleDao.deleteSchedule(scheduleId)
}