package com.vidler.vidler.util

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object SnackBarManager {
    private val _snackbarState = mutableStateOf<SnackbarData?>(null)
    val snackbarState: State<SnackbarData?> = _snackbarState

    fun show(
        message: String,
        actionLabel: String? = null,
        duration: Long? = 1500L,
        actionDismiss: Boolean? = true,
        onAction: (() -> Unit)? = null
    ) {
        _snackbarState.value = SnackbarData(message, actionLabel, actionDismiss, onAction)

        if (duration != null) CoroutineScope(Dispatchers.Main).launch {
            delay(duration)
            dismiss()
        }
    }

    fun dismiss() {
        _snackbarState.value = null
    }

    data class SnackbarData(
        val message: String,
        val actionLabel: String? = null,
        val actionDismiss: Boolean? = true,
        val onAction: (() -> Unit)? = null
    )
}
