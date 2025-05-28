package com.vidler.core.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity(tableName = "schedule")
data class Schedule(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val videos: List<Video>,
    val startTime: Date,
    val endTime: Date,
    val createdAt: Date = Date()
)