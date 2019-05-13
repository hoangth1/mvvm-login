package com.login.mvvm.login_test.utils

import android.util.Patterns

    fun String.isValidEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

    fun String.isValidPassword(): Boolean = this.length > 8

