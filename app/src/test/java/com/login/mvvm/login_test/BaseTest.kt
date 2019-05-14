package com.login.mvvm.login_test

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

open class BaseTest {

    @Before
    open fun setUp(){
        // make annotation work
        MockitoAnnotations.initMocks(this)

        //
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.trampoline() }
    }

    /**
     *  Call this fun for Object class to avoid non-null error in kotlin
     */
    fun <T> any(type: Class<T>): T {
        Mockito.any(type)
        return uninitialized()
    }

    fun <T> uninitialized(): T = null as T
}
