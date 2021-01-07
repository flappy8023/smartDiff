package com.flappy.smartdiff.presenter

import JYDAMEquip
import com.flappy.smartdiff.Preference
import com.flappy.smartdiff.base.BasePresenter
import com.flappy.smartdiff.bean.MaterialBean
import com.flappy.smartdiff.constant.Constant
import com.flappy.smartdiff.contract.DiffContract
import com.flappy.smartdiff.model.DiffModel
import damtest
import net.wimpi.modbus.util.ThreadPool

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

    override fun openLock(index:Int) {

        Thread({
            var tryCount = 0
            val equip = JYDAMEquip()
            val ip by Preference(Constant.KEY_TCP_IP,"192.168.10.1")
            equip.Init(ip,10000,254)
            if(!equip.IsConnect()) {
                equip.BeginConnect()
                while (!equip.IsConnect()) {
                    Thread.sleep(200)
                    tryCount++
                    if(tryCount>1){
                        mView?.showToast("设备未连接")
                        return@Thread
                    }
                }
            }
            equip.writeSignalDO(index, 1)
            Thread.sleep(20)
            val value = equip.readSignalDO(index)
            if(1==value){
                mView?.openSucc()
            }else{
                mView?.showToast("开启失败")
            }
            Thread.sleep(1000)
            equip.writeSignalDO(index,0)
            equip.DisConnect()

        }).start()
    }

    override fun createModel(): DiffModel {
        return DiffModel()
    }
}