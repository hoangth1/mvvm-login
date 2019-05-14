package com.login.mvvm.login_test.di

import android.provider.Contacts.SettingsColumns.KEY
import com.login.mvvm.login_test.data.AuthenApi
import com.login.mvvm.login_test.di.Properties.TIME_OUT
import com.login.mvvm.login_test.rx.RxErrorHandlingCallAdapterFactory
import com.login.mvvm.login_test.utils.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val networkModule = module(override = true) {
    single(name = "logging") { createHeaderInterceptor() }
    single(name = "header") { createLoggingInterceptor() }
    single { createOkHttpClient(get(name = "logging"), get(name = "header")) }
    single { createAppRetrofit(get()) }
}

object Properties {
    const val TIME_OUT = 10
}

fun createLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

fun createHeaderInterceptor(
): Interceptor = Interceptor { chain ->
    val request = chain.request()
    val newUrl = request.url().newBuilder()
        .build()
    val newRequest = request.newBuilder()
        .url(newUrl)
        .method(request.method(), request.body())
        .build()
    chain.proceed(newRequest)
}

fun createOkHttpClient(logging: Interceptor, header: Interceptor): OkHttpClient =
    OkHttpClient.Builder()
        .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        .addInterceptor(header)
        .addInterceptor(logging)
        .build()

fun createAppRetrofit(okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

fun createLAuthenApi(retrofit: Retrofit): AuthenApi {
    return  retrofit.create(AuthenApi::class.java)
}
