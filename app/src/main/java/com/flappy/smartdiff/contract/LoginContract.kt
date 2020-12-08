package com.flappy.smartdiff.contract

import com.flappy.smartdiff.base.BaseModel
import com.flappy.smartdiff.base.BaseView

interface LoginContract {
    interface ILoginView:BaseView{
        fun loginSuccess()
        fun loginFail(msg:String)
    }
    interface ILoginModel:BaseModel{
        fun login(name:String,pwd:String)
    }
    interface ILoginPresenter{
        fun login(name: String,pwd:String)
    }
}