package com.flappy.smartdiff.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<P:BasePresenter<*,*>>: AppCompatActivity(),BaseView {
    val mPresenter:P by lazy { createPreseneter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        mPresenter.attachView(this)
        initView()
    }
    abstract fun getLayoutId():Int

    abstract fun initView()

    abstract fun createPreseneter():P

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}