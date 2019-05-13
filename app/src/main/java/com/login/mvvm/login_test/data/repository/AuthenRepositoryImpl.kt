package com.login.mvvm.login_test.data.repository

import com.login.mvvm.login_test.data.AuthenApi
import com.login.mvvm.login_test.data.model.Token
import io.reactivex.Single

class AuthenRepositoryImpl(val api: AuthenApi) : AuthenRepository {
    override fun login(email: String, password: String): Single<Token> {
        return api.login(email, password)
    }
}