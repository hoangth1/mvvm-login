package com.login.mvvm.login_test.data.repository

import com.login.mvvm.login_test.data.model.Token
import io.reactivex.Single

interface AuthenRepository {
    fun login(email: String, password: String): Single<Token>
}