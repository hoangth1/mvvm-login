package com.login.mvvm.login_test.di

import com.login.mvvm.login_test.screen.ui.activity.MainActivityViewModel
import com.login.mvvm.login_test.screen.ui.fragment.login.LoginViewModel
import com.login.mvvm.login_test.screen.ui.fragment.main.MainViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


val viewModule = module {
    viewModel { MainActivityViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}
