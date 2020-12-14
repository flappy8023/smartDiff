package com.flappy.smartdiff.contract

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
    }
    interface IDiffModel:IBaseModel{
        fun getMaterial():List<MaterialBean>?
    }
    interface IDiffPresenter{
        fun getMaterials():List<MaterialBean>
    }
}