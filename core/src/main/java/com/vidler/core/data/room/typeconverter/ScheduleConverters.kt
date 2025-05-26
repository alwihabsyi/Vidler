package com.vidler.core.data.room.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vidler.core.domain.entity.Video
import java.util.Date

class ScheduleConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromVideoList(videos: List<Video>): String {
        val videoList = videos.map { it.copy(thumbnail = null) }
        return gson.toJson(videoList)
    }

    @TypeConverter
    fun toVideoList(data: String): List<Video> {
        val type = object : TypeToken<List<Video>>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(millis: Long): Date = Date(millis)
}