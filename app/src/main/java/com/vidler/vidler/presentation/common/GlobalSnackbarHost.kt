package com.vidler.vidler.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vidler.vidler.util.SnackBarManager

@Composable
fun GlobalSnackbarHost(modifier: Modifier = Modifier) {
    val snackbarData = SnackBarManager.snackbarState.value

    AnimatedVisibility(
        visible = snackbarData != null, enter = fadeIn(), exit = fadeOut()
    ) {
        Box(modifier = modifier.fillMaxHeight()) {
            SnackBar(
                message = snackbarData?.message.orEmpty(),
                actionLabel = snackbarData?.actionLabel.orEmpty(),
                onAction = {
                    snackbarData?.onAction?.invoke()
                    if (snackbarData?.actionDismiss == true) SnackBarManager.dismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}
