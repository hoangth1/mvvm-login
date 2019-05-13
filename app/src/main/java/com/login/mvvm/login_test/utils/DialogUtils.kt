package com.login.mvvm.login_test.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.login.mvvm.login_test.R


fun createLoadingDialog(context: Context?): AlertDialog {
    return AlertDialog.Builder(context)
        .setView(R.layout.layout_loading_diaglog).create().apply {
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
}

fun showLoadingDialog(alertDialog: AlertDialog) {
    alertDialog.show()
}

fun hideLoadingDialog(alertDialog: AlertDialog) {
    alertDialog.hide()
}

fun showMessageDiaglog(
    context: Context,
    title: String? = null,
    message: String? = null,
    positiveText: String? = null,
    positiveListener: (() -> Unit)? = null,
    negativeText: String? = null,
    negativeListener: (() -> Unit)? = null,
    cancelOnTouchOutside: Boolean = true
) {
    AlertDialog.Builder(context)
        .setMessage(message)
        .setPositiveButton(positiveText) { _, _ ->
            positiveListener?.invoke()
        }
        .setNegativeButton(negativeText) { _, _ ->
            negativeListener?.invoke()
        }
        .setTitle(title)
        .create().apply {
            setCanceledOnTouchOutside(cancelOnTouchOutside)
        }.show()
}
