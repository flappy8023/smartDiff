package com.flappy.smartdiff.contract

import com.flappy.smartdiff.base.IBaseModel
import com.flappy.smartdiff.base.IBaseView
import com.flappy.smartdiff.bean.DepotBean

/**
 * @FileName: SettingContract
 * @Author: luweiming
 * @Date: 2020/12/11 14:12
 * @Description:
 * @Version: 1.0
 */
interface SettingContract {
    interface ISettingView:IBaseView{}
    interface ISettingModel:IBaseModel{
        fun depot():DepotBean?
        fun saveDepot(depotBean: DepotBean):Long
    }
    interface ISettingPresenter{
        fun depot():DepotBean?
        fun saveDepot(depotBean: DepotBean):Boolean
    }
}