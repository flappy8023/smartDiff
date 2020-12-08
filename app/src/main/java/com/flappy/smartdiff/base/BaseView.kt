package com.flappy.smartdiff.base

interface BaseView {
    fun showLoading(s: String)
    fun loadSuccess()
    fun loadFail()
    fun showToast(msg: String)
}