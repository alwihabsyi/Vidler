package com.vidler.vidler.di

import com.vidler.vidler.presentation.collection.CollectionViewModel
import com.vidler.vidler.presentation.home.HomeViewModel
import com.vidler.vidler.presentation.schedule.ScheduleViewModel
import com.vidler.vidler.presentation.video.VideoPlayerViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { CollectionViewModel(get(), get()) }
    viewModel { ScheduleViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { VideoPlayerViewModel(get()) }
}
