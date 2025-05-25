package com.vidler.vidler.di

import com.vidler.vidler.presentation.collection.CollectionViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { CollectionViewModel(get(), get()) }
}
