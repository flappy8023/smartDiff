package com.flappy.smartdiff.model

import com.flappy.smartdiff.bean.MaterialBean
import com.flappy.smartdiff.contract.DiffContract
import com.flappy.smartdiff.contract.DiffSetContract
import com.flappy.smartdiff.dao.DepotDao
import com.flappy.smartdiff.db.MyDatabase

/**
 * @FileName: DiffSetModel
 * @Author: luweiming
 * @Date: 2020/12/11 14:13
 * @Description:
 * @Version: 1.0
 */
class DiffSetModel:DiffSetContract.IDiffSetMoel {
    val dao:DepotDao = MyDatabase.instance.depotDao()
    override fun getMaterials(): List<MaterialBean> {
        return dao.queryMaterials()
    }

    override fun updateMaterial(bean: MaterialBean) {
        dao.updateMaterial(bean)
    }

}