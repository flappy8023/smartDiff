package com.flappy.smartdiff.contract

import com.flappy.smartdiff.base.IBaseModel
import com.flappy.smartdiff.base.IBaseView

interface LoginContract {
    interface ILoginView:IBaseView{
        fun loginSuccess()
        fun loginFail(msg:String)
    }
    interface ILoginModel:IBaseModel{
        fun login(name:String,pwd:String)
    }
    interface ILoginPresenter{
        fun login(name: String,pwd:String)
    }
}