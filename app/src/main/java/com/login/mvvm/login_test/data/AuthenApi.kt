package com.login.mvvm.login_test.data

import com.login.mvvm.login_test.data.model.Token
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthenApi{
    @GET("login")
    fun login(@Query("email") email:String, @Query("password") password:String):Single<Token>
}