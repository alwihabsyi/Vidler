package com.vidler.vidler.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatDuration(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale("id", "ID"), "%d:%02d", minutes, seconds)
}

fun Date.toDateString(): String {
    val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy H:mm", Locale("id", "ID"))
    return formatter.format(this)
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}