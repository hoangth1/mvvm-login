package com.login.mvvm.login_test

import android.app.Application
import com.login.mvvm.login_test.di.modules
import org.koin.android.ext.android.startKoin

class MainApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin(this, modules)
    }
}