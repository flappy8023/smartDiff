package com.flappy.smartdiff.model

import com.flappy.smartdiff.bean.DepotBean
import com.flappy.smartdiff.bean.MaterialBean
import com.flappy.smartdiff.contract.SettingContract
import com.flappy.smartdiff.dao.DepotDao
import com.flappy.smartdiff.db.MyDatabase

/**
 * @FileName: SettingModel
 * @Author: luweiming
 * @Date: 2020/12/11 14:14
 * @Description:
 * @Version: 1.0
 */
class SettingModel:SettingContract.ISettingModel {
    private val dao:DepotDao = MyDatabase.instance.depotDao()
    override fun depot(): DepotBean? {
        return dao.getDepots()
    }

    override fun saveDepot(depotBean: DepotBean): Long {
        dao.deleteAll()
        dao.deleteAllMaterials()
        val rowid = dao.insert(depotBean)
        val nameStart = depotBean.nameStart
        val start = nameStart.substring(nameStart.length-2,nameStart.length).toInt()
        val pre = nameStart.substring(0,nameStart.length-2)
        val materials = mutableListOf<MaterialBean>()
        for (i in start..start+depotBean.count-1){
            val id = if(i<10) "0"+i else i.toString()
            val bean = MaterialBean(pre+id,"","","")
            materials.add(bean)
        }
        dao.insertAllMaterials(materials)
        return rowid
    }
}