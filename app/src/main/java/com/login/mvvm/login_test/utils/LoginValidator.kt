package com.login.mvvm.login_test.utils

import android.util.Patterns

object LoginValidator {

    fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isValidPassword(passWd: String): Boolean = passWd.length >= 6
}
