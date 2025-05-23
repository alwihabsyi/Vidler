package com.vidler.vidler.presentation.collection

import com.vidler.core.domain.entity.Video

data class CollectionState(
    val videos: List<Video> = emptyList()
)
