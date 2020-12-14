package com.flappy.smartdiff.presenter

import com.flappy.smartdiff.base.BasePresenter
import com.flappy.smartdiff.bean.MaterialBean
import com.flappy.smartdiff.contract.DiffContract
import com.flappy.smartdiff.model.DiffModel

/**
 * @FileName: DiffPresenter
 * @Author: luweiming
 * @Date: 2020/12/11 11:02
 * @Description:
 * @Version: 1.0
 */
class DiffPresenter:DiffContract.IDiffPresenter,BasePresenter<DiffContract.IDiffView,DiffModel>() {
    override fun getMaterials():List<MaterialBean> {
//        val strings = mutableListOf<String>()
//        mModel.getMaterial().forEach {
//            strings.add(it.id)
//        }
        return mModel.getMaterial()
    }

    override fun createModel(): DiffModel {
        return DiffModel()
    }
}