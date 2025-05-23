package com.vidler.vidler.util

import java.util.Locale

fun Long.formatDuration(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale("id", "ID"), "%d:%02d", minutes, seconds)
}