package com.flappy.smartdiff.presenter

import com.flappy.smartdiff.base.BasePresenter
import com.flappy.smartdiff.bean.MaterialBean
import com.flappy.smartdiff.contract.DiffSetContract
import com.flappy.smartdiff.dao.DepotDao
import com.flappy.smartdiff.db.MyDatabase
import com.flappy.smartdiff.model.DiffSetModel

/**
 * @FileName: DiffSetPresenter
 * @Author: luweiming
 * @Date: 2020/12/11 14:15
 * @Description:
 * @Version: 1.0
 */
class DiffSetPresenter : DiffSetContract.IDiffSetPreseneter,
    BasePresenter<DiffSetContract.IDiffSetView, DiffSetContract.IDiffSetMoel>() {
    val dao: DepotDao = MyDatabase.instance.depotDao()
    override fun createModel(): DiffSetContract.IDiffSetMoel {
        return DiffSetModel()
    }

    fun getMaterials(): List<MaterialBean> {
        return dao.queryMaterials()
    }

    fun updateMaterial(curMaterial: MaterialBean) {
        dao.updateMaterial(curMaterial)
    }
}