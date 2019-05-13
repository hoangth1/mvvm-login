package com.login.mvvm.login_test.data

import com.login.mvvm.login_test.data.model.Token
import io.reactivex.Single
import retrofit2.http.GET

interface AuthenApi{
    @GET("aaaaaa")
    fun login(emai:String, password:String):Single<Token>
}