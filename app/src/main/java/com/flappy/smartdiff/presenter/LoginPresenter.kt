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
        if (!mModel.userExist(name)) {
            mView?.loginFail(MyApplication.mContext.getString(R.string.user_does_not_exist))
        } else {
            Constant.curUser = mModel.getUserByName(name)
            if (Constant.curUser?.pwd == pwd) {
                Constant.isAdmin = name == Constant.DEFAULT_USERNAME
                mView?.loginSuccess()
            } else {
                mView?.loginFail(MyApplication.mContext.getString(R.string.wrong_pwd))
            }
        }
    }


}