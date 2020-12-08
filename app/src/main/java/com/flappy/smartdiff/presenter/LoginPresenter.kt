package com.flappy.smartdiff.presenter

import com.flappy.smartdiff.MyApplication
import com.flappy.smartdiff.R
import com.flappy.smartdiff.base.BasePresenter
import com.flappy.smartdiff.constant.Constant
import com.flappy.smartdiff.contract.LoginContract
import com.flappy.smartdiff.model.LoginModel

class LoginPresenter : BasePresenter<LoginContract.ILoginView, LoginModel>(),
    LoginContract.ILoginPresenter {
    override fun createModel(): LoginModel {
        return LoginModel()
    }

    override fun login(name: String, pwd: String) {
        if (Constant.DEFAULT_USERNAME.equals(name) && Constant.DEFAULT_PWD.equals(pwd)) {
            mView?.loginSuccess()
        } else if (!checkUserName(name)) {
            mView?.loginFail(MyApplication.mContext.getString(R.string.user_does_not_exist))
        }else{
            mView?.loginFail(MyApplication.mContext.getString(R.string.wrong_pwd))
        }
    }

    fun checkUserName(name: String) = Constant.DEFAULT_USERNAME.equals(name)

}