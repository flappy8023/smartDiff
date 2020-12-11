package com.flappy.smartdiff.presenter

import com.flappy.smartdiff.base.BasePresenter
import com.flappy.smartdiff.contract.DiffSetContract
import com.flappy.smartdiff.model.DiffSetModel

/**
 * @FileName: DiffSetPresenter
 * @Author: luweiming
 * @Date: 2020/12/11 14:15
 * @Description:
 * @Version: 1.0
 */
class DiffSetPresenter:DiffSetContract.IDiffSetPreseneter,BasePresenter<DiffSetContract.IDiffSetView,DiffSetContract.IDiffSetMoel>() {
    override fun createModel(): DiffSetContract.IDiffSetMoel {
        return DiffSetModel()
    }
}