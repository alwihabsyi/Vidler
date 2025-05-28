package com.vidler.core.di

import androidx.room.Room
import androidx.work.WorkManager
import com.vidler.core.data.repository.ScheduleRepositoryImpl
import com.vidler.core.data.room.AppDatabase
import com.vidler.core.data.room.RoomDataSource
import com.vidler.core.domain.helper.DownloadHelper
import com.vidler.core.domain.helper.FileHelper
import com.vidler.core.domain.repository.ScheduleRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val helperModule = module {
    single { FileHelper(androidContext()) }
    single { DownloadHelper(androidContext(), get()) }
    single { WorkManager.getInstance(androidContext()) }
}

val databaseModule = module {
    single { RoomDataSource(get()) }
    factory { get<AppDatabase>().scheduleDao() }
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration(false).build()
    }
}

val repositoryModule = module {
    single<ScheduleRepository> { ScheduleRepositoryImpl(get(), get()) }
}