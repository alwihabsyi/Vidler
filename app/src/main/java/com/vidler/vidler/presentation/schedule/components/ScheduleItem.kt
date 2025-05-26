package com.vidler.vidler.presentation.schedule.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vidler.core.domain.entity.Schedule
import com.vidler.vidler.util.toDateString
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleItem(modifier: Modifier = Modifier, scheduleItem: Schedule, onItemLongClick: (() -> Unit)? = null, onItemClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(15.dp))
            .combinedClickable(
                onClick = { onItemClick() },
                onLongClick = { onItemLongClick?.invoke() }
            )
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.White)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = scheduleItem.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Start : ${scheduleItem.startTime.toDateString()}\nEnd : ${scheduleItem.endTime.toDateString()}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun ScheduleItemPreview() {
    ScheduleItem(scheduleItem = Schedule(
        id = 1,
        name = "Jadwal Preview",
        videos = emptyList(),
        startTime = Date(),
        endTime = Date()
    )
    ) {

    }
}