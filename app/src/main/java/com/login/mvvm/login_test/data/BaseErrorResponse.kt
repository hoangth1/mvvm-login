package com.login.mvvm.login_test.data

import com.google.gson.annotations.SerializedName

data class BaseErrorResponse(
    @SerializedName("status_message") val statusMessage: String,
    @SerializedName("status_code") val statusCode: Int
)
