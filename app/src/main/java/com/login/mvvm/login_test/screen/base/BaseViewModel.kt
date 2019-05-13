package com.login.mvvm.login_test.screen.base

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.login.mvvm.login_test.R
import com.login.mvvm.login_test.data.BaseException
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.net.SocketTimeoutException
import java.net.UnknownHostException


abstract class BaseViewModel(val resources: Resources) : ViewModel() {
    val compositeDisposable = CompositeDisposable()
    val isLoading = MutableLiveData<Boolean>().apply { value = false }
    val error = MutableLiveData<String>()


    open fun onError(throwable: Throwable) {
        when (throwable) {
            is BaseException -> {
                when (throwable.cause) {
                    is UnknownHostException -> error.value = resources.getString(R.string.message_no_internet)
                    is SocketTimeoutException -> error.value = resources.getString(R.string.message_timeout)
                    else -> {
                        when (throwable.errorType) {
                            BaseException.HTTP -> error.value = throwable.response?.message()
                            BaseException.SERVER -> error.value = throwable.errorResponse?.statusMessage
                            BaseException.UNEXPECTED -> error.value = throwable.message
                        }

                    }
                }
            }
            else -> error.value = throwable.message
        }
    }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
    }

}
