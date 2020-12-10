package com.flappy.smartdiff.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xiasuhuei321.loadingdialog.view.LoadingDialog

abstract class BaseFragment<P : BasePresenter<*, *>> : Fragment(), IBaseView {
    val mPresenter: P by lazy { createPresenter() }
    val loading: LoadingDialog by lazy { LoadingDialog(activity) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        mPresenter.attachView(this)
        initView()
        return view
    }

    abstract fun initView()
    abstract fun getLayoutId(): Int
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
        showToast(msg)
    }

    abstract fun createPresenter(): P
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}