package com.flappy.smartdiff.presenter

import com.flappy.smartdiff.base.BasePresenter
import com.flappy.smartdiff.contract.MainContract
import com.flappy.smartdiff.model.MainModel

/**
 * @FileName: MainPreseneter
 * @Author: luweiming
 * @Date: 2020/12/10 17:37
 * @Description:
 * @Version: 1.0
 */
class MainPreseneter:BasePresenter<MainContract.IMainView,MainContract.IMainModel>(),MainContract.IMainPresenter {
    override fun createModel(): MainContract.IMainModel {
        return MainModel()
    }
}