package com.vidler.core.domain.entity

import android.graphics.Bitmap

data class Video(
    val name: String,
    val path: String,
    val duration: Long,
    val thumbnail: Bitmap?
)
