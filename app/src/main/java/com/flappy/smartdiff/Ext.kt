package com.flappy.smartdiff

import android.content.Context
import android.widget.Toast
import com.flappy.smartdiff.constant.Constant

fun Context.showToast(msg: String) {
    Constant.showToast?.apply {
        setText(msg)
        show()
    } ?: run {
        Toast.makeText(this.applicationContext, msg, Toast.LENGTH_LONG).apply {
            Constant.showToast = this
        }.show()
    }
}