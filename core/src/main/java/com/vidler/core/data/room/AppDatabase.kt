package com.vidler.core.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vidler.core.data.room.dao.ScheduleDao
import com.vidler.core.data.room.typeconverter.ScheduleConverters
import com.vidler.core.domain.entity.Schedule

@Database(entities = [Schedule::class], version = 1)
@TypeConverters(ScheduleConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
}