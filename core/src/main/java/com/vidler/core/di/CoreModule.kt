package com.vidler.core.di

import com.vidler.core.domain.helper.FileHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val helperModule = module {
    single { FileHelper(androidContext()) }
}