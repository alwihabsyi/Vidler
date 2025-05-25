package com.vidler.vidler.presentation.collection

import com.vidler.core.domain.entity.Video
import com.vidler.core.domain.helper.DownloadHelper

data class CollectionState(
    val videos: List<Video> = emptyList(),
    val downloadProgress: DownloadHelper.DownloadResult? = null
)
