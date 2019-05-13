package com.login.mvvm.login_test.screen.ui.fragment.login

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.login.mvvm.login_test.data.repository.AuthenRepository
import com.login.mvvm.login_test.rx.ScheduleProvider
import com.login.mvvm.login_test.screen.base.BaseViewModel
import com.login.mvvm.login_test.utils.isValidEmail
import io.reactivex.Scheduler

class LoginViewModel(
    resources: Resources,
    val authenRepository: AuthenRepository,
    val scheduleProvider: ScheduleProvider
) : BaseViewModel(resources) {
    val loginSuccess = MutableLiveData<Boolean>()

    fun login(email: String, password: String) {
        if (email.isValidEmail() && password.isValidEmail()) {
            error.value = "Email or Password invalid"
        } else {
            addDisposable(
                authenRepository.login(email, password)
                    .subscribeOn(scheduleProvider.io())
                    .observeOn(scheduleProvider.ui())
                    .subscribe({
                        loginSuccess.value = true
                    }, {
                        onError(it)
                    })
            )
        }
    }
}