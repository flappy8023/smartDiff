package com.flappy.smartdiff.base

interface IBaseView {
    fun showLoading(s: String)
    fun loadSuccess()
    fun loadFail()
    fun showToast(msg: String)
}