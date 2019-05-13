package com.login.mvvm.login_test.rx

import io.reactivex.Scheduler

interface ScheduleProvider {
    fun ui(): Scheduler

    fun io(): Scheduler

    fun computation(): Scheduler
}
