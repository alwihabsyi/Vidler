package com.vidler.core.data.repository

import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.vidler.core.data.room.RoomDataSource
import com.vidler.core.data.worker.ScheduleNotificationWorker
import com.vidler.core.domain.entity.Schedule
import com.vidler.core.domain.repository.ScheduleRepository
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class ScheduleRepositoryImpl(
    private val roomDataSource: RoomDataSource,
    private val workManager: WorkManager
): ScheduleRepository {
    override fun get(): Flow<List<Schedule>> =
        roomDataSource.getSchedule()

    override fun getById(id: String): Flow<Schedule?> =
        roomDataSource.getScheduleById(id)

    override suspend fun insert(schedule: Schedule) =
        roomDataSource.insertSchedule(schedule).also {
            scheduleNotificationWorker(schedule)
        }

    override suspend fun delete(scheduleId: String) =
        roomDataSource.deleteSchedule(scheduleId).also {
            cancelNotificationWorker(scheduleId)
        }

    private fun scheduleNotificationWorker(schedule: Schedule) {
        val delay = schedule.startTime.time - System.currentTimeMillis()
        if (delay <= 0) return

        val data = Data.Builder()
            .putString("schedule_id", schedule.id)
            .putString("title", schedule.name)
            .putString("message", "Video schedule is starting!")
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ScheduleNotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("schedule_${schedule.id}")
            .build()

        workManager.enqueue(workRequest)
    }

    private fun cancelNotificationWorker(scheduleId: String) =
        workManager.cancelAllWorkByTag("schedule_${scheduleId}")
}