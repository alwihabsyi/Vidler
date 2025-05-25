package com.vidler.vidler.presentation.collection.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri

@Composable
fun FileInputDialog(modifier: Modifier = Modifier, showDialog: Boolean, onStoragePic: (List<Uri>) -> Unit,onDismiss: () -> Unit) {
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var isUrlSelected by remember { mutableStateOf(false) }
    var urlInput by remember { mutableStateOf(TextFieldValue()) }

    AnimatedVisibility(visible = showDialog) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                modifier = modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Choose your video source:")
                Spacer(modifier = Modifier.padding(top = 20.dp))
                Button(
                    colors = ButtonColors(
                        MaterialTheme.colorScheme.onSurfaceVariant,
                        Color.White,
                        disabledContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
                        disabledContentColor = Color.White
                    ), shape = RoundedCornerShape(10.dp), onClick = { isUrlSelected = true }) {
                    Text("From URL")
                }
                Button(
                    colors = ButtonColors(
                        MaterialTheme.colorScheme.onSurfaceVariant,
                        Color.White,
                        disabledContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
                        disabledContentColor = Color.White
                    ), shape = RoundedCornerShape(10.dp), onClick = { isUrlSelected = false }) {
                    Text("From Storage")
                }

                if (isUrlSelected) {
                    UrlInputDialog(urlInput) { newUrl ->
                        urlInput = newUrl
                        videoUri = newUrl.text.toUri()
                        onDismiss()
                    }
                }

                if (!isUrlSelected) {
                    StorageInputDialog { uris ->
                        onStoragePic(uris)
                        onDismiss()
                    }
                }

                videoUri?.let {}
            }
        }
    }
}

@Composable
fun UrlInputDialog(urlInput: TextFieldValue, onSubmit: (TextFieldValue) -> Unit) {
    var inputValue by remember { mutableStateOf(urlInput) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Enter Video URL:")
        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Button(
            colors = ButtonColors(
                MaterialTheme.colorScheme.onSurfaceVariant,
                Color.White,
                disabledContainerColor = MaterialTheme.colorScheme.inverseOnSurface,
                disabledContentColor = Color.White
            ), shape = RoundedCornerShape(10.dp), onClick = { onSubmit(inputValue) }) {
            Text("Submit")
        }
    }
}

@Composable
fun StorageInputDialog(onUrisSelected: (List<Uri>) -> Unit) {
    val pickVideos =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris: List<Uri> ->
            if (uris.isNotEmpty()) {
                onUrisSelected(uris)
            }
        }

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp),
        onClick = { pickVideos.launch(arrayOf("video/*")) }
    ) {
        Text("Select Videos from Storage")
    }
}