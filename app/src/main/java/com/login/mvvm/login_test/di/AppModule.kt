package com.login.mvvm.login_test.di

import com.login.mvvm.login_test.rx.ScheduleProvider
import com.login.mvvm.login_test.rx.SchedulerProviderImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module

val appModule = module {
    single { androidApplication().resources }
    single { createSchedule() }
}

fun createSchedule(): ScheduleProvider = SchedulerProviderImpl()

val modules = arrayListOf(
    appModule, repositoryModule, networkModule, viewModule
)
