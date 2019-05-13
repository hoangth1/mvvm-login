package com.login.mvvm.login_test.screen.ui.fragment.login

import com.login.mvvm.login_test.BR
import com.login.mvvm.login_test.R
import com.login.mvvm.login_test.databinding.FragmentLoginBinding
import com.login.mvvm.login_test.screen.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
    companion object {
        val TAG = "LoginFragment"
        fun newInstance() = LoginFragment()
    }

    override val viewModel: LoginViewModel by viewModel()
    override val layoutResource: Int = R.layout.fragment_login
    override val viewModelVariable: Int = BR.viewModel

    override fun initComponent() {

    }
}