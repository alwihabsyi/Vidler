package com.vidler.vidler.presentation.schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vidler.core.domain.entity.Schedule
import com.vidler.core.domain.entity.Video
import com.vidler.vidler.presentation.collection.components.CollectionItem
import com.vidler.vidler.util.SnackBarManager
import com.vidler.vidler.util.toDateString
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScheduleScreen(
    modifier: Modifier = Modifier,
    videos: List<Video>,
    onBack: () -> Unit = {},
    onEvent: (ScheduleEvent) -> Unit
) {
    val context = LocalContext.current
    var scheduleName by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf<Date?>(null) }
    var endTime by remember { mutableStateOf<Date?>(null) }
    var selectedVideos by remember { mutableStateOf(listOf<Video>()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Create Schedule") }, navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            })
        }) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(17.dp)
        ) {
            OutlinedTextField(
                value = scheduleName,
                onValueChange = { scheduleName = it },
                label = { Text("Schedule Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    showDateTimePicker(context) { selected ->
                        startTime = selected
                    }
                }) {
                Text(text = startTime?.let { "Start: ${it.toDateString()}" } ?: "Pick Start Time")
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    showDateTimePicker(context) { selected ->
                        endTime = selected
                    }
                }) {
                Text(text = endTime?.let { "End: ${it.toDateString()}" } ?: "Pick End Time")
            }

            Text(
                "Video Collection",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(videos) { video ->
                    val isSelected = selectedVideos.contains(video)
                    CollectionItem(videoItem = video) {
                        selectedVideos = if (isSelected) selectedVideos - video
                        else selectedVideos + video
                    }
                }
            }

            Text(
                "Selected Videos",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(selectedVideos) { video ->
                    CollectionItem(videoItem = video) {
                        selectedVideos = selectedVideos - video
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (scheduleName.isNotBlank() && startTime != null && endTime != null && selectedVideos.isNotEmpty()) {
                        onEvent(
                            ScheduleEvent.OnAddSchedule(
                                Schedule(
                                    name = scheduleName,
                                    videos = selectedVideos,
                                    startTime = startTime!!,
                                    endTime = endTime!!
                                )
                            )
                        )
                        onBack()
                    } else SnackBarManager.show("Harap isi semua bagian!", "Tutup")
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Schedule")
            }
        }
    }
}

fun showDateTimePicker(context: Context, onDateTimeSelected: (Date) -> Unit) {
    val calendar = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            TimePickerDialog(
                context, { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    onDateTimeSelected(calendar.time)
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
            ).show()
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}