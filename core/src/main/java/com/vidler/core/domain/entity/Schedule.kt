package com.vidler.core.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val videos: List<Video>,
    val startTime: Date,
    val endTime: Date,
    val createdAt: Date = Date()
)