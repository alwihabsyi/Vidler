package com.vidler.core.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vidler.core.domain.entity.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedule")
    fun getSchedules(): Flow<List<Schedule>>

    @Insert
    suspend fun insertSchedule(schedule: Schedule)

    @Query("DELETE FROM schedule WHERE id = :scheduleId")
    suspend fun deleteSchedule(scheduleId: Int)
}