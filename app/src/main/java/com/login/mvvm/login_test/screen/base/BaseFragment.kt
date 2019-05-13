package com.login.mvvm.login_test.screen.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.login.mvvm.login_test.R
import com.login.mvvm.login_test.utils.createLoadingDialog
import com.login.mvvm.login_test.utils.hideLoadingDialog
import com.login.mvvm.login_test.utils.showLoadingDialog
import com.login.mvvm.login_test.utils.showMessageDiaglog


abstract class BaseFragment<ViewBinding : ViewDataBinding, ViewModel : BaseViewModel> : Fragment() {
    abstract val viewModel: ViewModel
    lateinit var viewBinding: ViewBinding
    abstract val layoutResource: Int
    abstract val viewModelVariable: Int
    lateinit var loadingDiaglog: AlertDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<ViewBinding>(inflater, layoutResource, container, false).apply {
            viewBinding = this
            lifecycleOwner = viewLifecycleOwner
            setVariable(viewModelVariable, viewModel)
            loadingDiaglog = createLoadingDialog(context)
        }.root.apply(fun View.() {
            isClickable = true
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, Observer {
                handleShowLoading(it ?: return@Observer)
            })
            error.observe(viewLifecycleOwner, Observer {
                showError(it ?: return@Observer)
            })
        }

        initComponent()

    }

    abstract fun initComponent()

    fun addFragment(container: Int, fragment: Fragment, tag: String, isAddBackStack: Boolean = true) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            add(container, fragment)
            if (isAddBackStack) {
                addToBackStack(tag)
            }
            commit()
        }

    }

    fun replaceFragment(container: Int, fragment: Fragment, tag: String, isAddBackStack: Boolean = true) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(container, fragment)
            if (isAddBackStack) {
                addToBackStack(tag)
            }
            commit()
        }

    }

    fun addChildFragment(container: Int, fragment: Fragment, tag: String, isAddBackStack: Boolean) {
        childFragmentManager.beginTransaction().apply {
            add(container, fragment)
            if (isAddBackStack) {
                addToBackStack(tag)
            }
            commit()
        }
    }

    fun replaceChildFragment(container: Int, fragment: Fragment, tag: String, isAddBackStack: Boolean) {
        childFragmentManager.beginTransaction().apply {
            replace(container, fragment)
            if (isAddBackStack) {
                addToBackStack(tag)
            }
            commit()
        }
    }

    private fun showError(error: String) {
        showMessageDiaglog(
            context = context ?: return, message = error,
            negativeText = getString(R.string.ok)
        )
    }

    open fun handleShowLoading(isLoading: Boolean) {
        if (isLoading) showLoadingDialog(alertDialog = loadingDiaglog) else hideLoadingDialog(loadingDiaglog)
    }
}
