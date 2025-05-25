package com.vidler.vidler.presentation.collection

import android.net.Uri

sealed class CollectionEvent {
    data class OnStorageMove(val uris: List<Uri>): CollectionEvent()
    data class OnDownload(val name: String, val url: String): CollectionEvent()
}