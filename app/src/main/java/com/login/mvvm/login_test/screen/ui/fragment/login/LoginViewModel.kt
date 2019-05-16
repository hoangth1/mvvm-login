package com.login.mvvm.login_test.screen.ui.fragment.login

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.login.mvvm.login_test.data.model.Token
import com.login.mvvm.login_test.data.repository.AuthenRepository
import com.login.mvvm.login_test.rx.ScheduleProvider
import com.login.mvvm.login_test.screen.base.BaseViewModel
import com.login.mvvm.login_test.utils.LoginValidator
import io.reactivex.Scheduler
import java.util.concurrent.TimeUnit

class LoginViewModel(
    resources: Resources,
    val authenRepository: AuthenRepository,
    val scheduleProvider: ScheduleProvider
) : BaseViewModel(resources) {

    val isLoginSuccess = MutableLiveData<Boolean>().apply { value = false }

    var token: Token? = null
    fun login(email: String, password: String) {
        if (LoginValidator.isValidEmail(email).not() && LoginValidator.isValidPassword(password).not()) {
            error.value = "Email or Password invalid"
        } else {
            addDisposable(
                authenRepository.login(email, password)
                    .subscribeOn(scheduleProvider.io())
                    .observeOn(scheduleProvider.ui())
                    .doOnSubscribe {
                        isLoading.value = true
                    }
                    .subscribe({
                        this@LoginViewModel.token = it
                        isLoading.value = false
                        isLoginSuccess.value = it?.accessToken != null &&
                                it.refreshToken != null
                    }, {
                        onError(it)
                        isLoading.value = false
                    })
            )
        }
    }

    fun abc () {

    }
}