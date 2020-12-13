package com.flappy.smartdiff.model

import com.flappy.smartdiff.bean.MaterialBean
import com.flappy.smartdiff.contract.DiffContract
import com.flappy.smartdiff.db.MyDatabase

/**
 * @FileName: DiffModel
 * @Author: luweiming
 * @Date: 2020/12/11 11:01
 * @Description:
 * @Version: 1.0
 */
class DiffModel:DiffContract.IDiffModel {
    val dao = MyDatabase.instance.depotDao()
    override fun getMaterial() :List<MaterialBean>{
        return dao.queryMaterials()
    }
}