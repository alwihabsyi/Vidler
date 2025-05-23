package com.vidler.vidler

import android.app.Application
import com.vidler.core.di.helperModule
import com.vidler.vidler.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class VidlerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@VidlerApplication)
            modules(listOf(
                viewModelModule,
                helperModule
            ))
        }
    }
}