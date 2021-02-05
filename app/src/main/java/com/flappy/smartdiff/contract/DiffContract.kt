package com.flappy.smartdiff.contract

import android.app.Activity
import android.content.Context
import com.flappy.smartdiff.base.IBaseModel
import com.flappy.smartdiff.base.IBaseView
import com.flappy.smartdiff.bean.MaterialBean

/**
 * @FileName: DiffContract
 * @Author: luweiming
 * @Date: 2020/12/11 9:22
 * @Description:
 * @Version: 1.0
 */
interface DiffContract {
    interface IDiffView:IBaseView{
        fun showMaterials()
        fun openSucc()
        fun getMyContext():Context?
    }
    interface IDiffModel:IBaseModel{
        fun getMaterial():List<MaterialBean>?
    }
    interface IDiffPresenter{
        fun getMaterials():List<MaterialBean>
        fun openLock(index:Int)
        fun sendPihao(index: Int,pihao:String)
    }

}