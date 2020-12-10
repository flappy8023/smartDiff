package com.flappy.smartdiff.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flappy.smartdiff.toast
import com.xiasuhuei321.loadingdialog.view.LoadingDialog

abstract class BaseActivity<P : BasePresenter<*, *>> : AppCompatActivity(), IBaseView {
    val mPresenter: P by lazy { createPreseneter() }
    val loading: LoadingDialog by lazy { LoadingDialog(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        mPresenter.attachView(this)
        initView()
    }

    abstract fun getContentView():View
    abstract fun initView()

    abstract fun createPreseneter(): P

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun showLoading(s: String) {
        loading.setLoadingText(s).show()
    }

    override fun loadFail() {
        loading.loadFailed()
    }

    override fun loadSuccess() {
        loading.loadSuccess()
    }

    override fun showToast(msg: String) {
        toast(msg)
    }
}