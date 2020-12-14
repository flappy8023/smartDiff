package com.flappy.smartdiff.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flappy.smartdiff.toast
import com.xiasuhuei321.loadingdialog.view.LoadingDialog
import org.greenrobot.eventbus.EventBus

abstract class BaseFragment<P : BasePresenter<*, *>> : Fragment(), IBaseView {
    val mPresenter: P by lazy { createPresenter() }
    val loading: LoadingDialog by lazy { LoadingDialog(activity) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mPresenter.attachView(this)
        val view = getRootView(inflater)
        initView()
        return view
    }

    override fun onStart() {
        super.onStart()
    }
    abstract fun getRootView(inflater: LayoutInflater):View
    abstract fun initView()
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
        context?.toast(msg)
    }

    abstract fun createPresenter(): P
    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}