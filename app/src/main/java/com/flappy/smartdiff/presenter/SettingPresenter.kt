package com.flappy.smartdiff.presenter

import com.flappy.smartdiff.base.BasePresenter
import com.flappy.smartdiff.contract.SettingContract
import com.flappy.smartdiff.model.SettingModel

/**
 * @FileName: SettingPresenter
 * @Author: luweiming
 * @Date: 2020/12/11 14:20
 * @Description:
 * @Version: 1.0
 */
class SettingPresenter:BasePresenter<SettingContract.ISettingView,SettingModel>(),
    SettingContract.ISettingPresenter {
    override fun createModel(): SettingModel {
        return SettingModel()
    }
}