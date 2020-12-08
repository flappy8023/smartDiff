package com.flappy.smartdiff.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.flappy.smartdiff.R
import com.flappy.smartdiff.base.BaseActivity
import com.flappy.smartdiff.contract.LoginContract
import com.flappy.smartdiff.databinding.ActivityLoginBinding
import com.flappy.smartdiff.presenter.LoginPresenter

class LoginActivity : BaseActivity<LoginPresenter>(), LoginContract.ILoginView {

    private lateinit var binding: ActivityLoginBinding

    override fun initView() {
        binding.btLogin.setOnClickListener {
            mPresenter.login(binding.etUserName.text.toString(),binding.etPwd.text.toString())
        }

    }

    override fun createPreseneter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun loginSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun loginFail(msg: String) {
        showToast(msg)
    }

    override fun getContentView(): View {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        return binding.root
    }

}