package com.flappy.smartdiff.base

import androidx.fragment.app.Fragment

class BaseFragment: Fragment(),BaseView {
    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }

    override fun showToast(msg: String) {
        showToast(msg)
    }
}