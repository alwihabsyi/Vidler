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

@Composable
fun FileInputDialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onStoragePic: (List<Uri>) -> Unit,
    onUrl: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var isUrlSelected by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf(TextFieldValue()) }
    var urlInput by remember { mutableStateOf(TextFieldValue()) }
    val pickVideos =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris: List<Uri> ->
            if (uris.isNotEmpty()) {
                onStoragePic(uris)
                onDismiss()
            }
        }

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
                    ), shape = RoundedCornerShape(10.dp), onClick = { pickVideos.launch(arrayOf("video/*")) }) {
                    Text("From Storage")
                }

                if (isUrlSelected) {
                    UrlInputDialog(nameInput, urlInput) { newName, newUrl ->
                        nameInput = newName
                        urlInput = newUrl
                        onUrl(newName.text, newUrl.text)
                        onDismiss()
                    }
                }
            }
        }
    }
}

@Composable
fun UrlInputDialog(
    nameInput: TextFieldValue,
    urlInput: TextFieldValue,
    onSubmit: (TextFieldValue, TextFieldValue) -> Unit
) {
    var nameInputValue by remember { mutableStateOf(nameInput) }
    var inputValue by remember { mutableStateOf(urlInput) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Enter Video Name:")
        OutlinedTextField(
            value = nameInputValue,
            onValueChange = { nameInputValue = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
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
            ),
            shape = RoundedCornerShape(10.dp),
            onClick = { onSubmit(nameInputValue, inputValue) }) {
            Text("Submit")
        }
    }
}