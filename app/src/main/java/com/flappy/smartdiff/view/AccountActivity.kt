package com.flappy.smartdiff.view

import android.view.View
import com.flappy.smartdiff.base.BaseActivity
import com.flappy.smartdiff.contract.AccountContract
import com.flappy.smartdiff.databinding.ActivityAccountBinding
import com.flappy.smartdiff.presenter.AccountPresenter

class AccountActivity : BaseActivity<AccountPresenter>(), AccountContract.IAccountView{
    private lateinit var binding: ActivityAccountBinding
    override fun getContentView(): View {
        binding = ActivityAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {

    }

    override fun createPreseneter(): AccountPresenter {
        return AccountPresenter()
    }
}