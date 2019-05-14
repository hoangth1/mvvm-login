package com.login.mvvm.login_test.di

import com.login.mvvm.login_test.data.repository.AuthenRepository
import com.login.mvvm.login_test.data.repository.AuthenRepositoryImpl
import org.koin.dsl.module.module

val repositoryModule = module {
    single<AuthenRepository> {AuthenRepositoryImpl(get())}
}
