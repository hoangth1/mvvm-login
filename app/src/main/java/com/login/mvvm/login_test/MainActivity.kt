package com.login.mvvm.login_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.login.mvvm.login_test.screen.ui.fragment.login.LoginFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.container, LoginFragment.newInstance(), LoginFragment.TAG)
                commit()
            }
        }
    }
}
