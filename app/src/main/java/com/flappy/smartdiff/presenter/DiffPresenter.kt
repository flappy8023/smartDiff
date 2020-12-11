package com.flappy.smartdiff.presenter

import com.flappy.smartdiff.base.BasePresenter
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
    override fun getMaterials() {
        TODO("Not yet implemented")
    }

    override fun createModel(): DiffModel {
        TODO("Not yet implemented")
    }
}