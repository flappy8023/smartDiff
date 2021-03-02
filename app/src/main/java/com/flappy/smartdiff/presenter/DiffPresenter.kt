package com.flappy.smartdiff.presenter

import JYDAMEquip
import com.flappy.smartdiff.Preference
import com.flappy.smartdiff.base.BasePresenter
import com.flappy.smartdiff.bean.MaterialBean
import com.flappy.smartdiff.constant.Constant
import com.flappy.smartdiff.contract.DiffContract
import com.flappy.smartdiff.model.DiffModel
import com.flappy.smartdiff.util.tcp.PacketBuffer
import com.flappy.smartdiff.util.tcp.bean.ServerModel
import com.flappy.smartdiff.util.tcp.listener.OnSessionListener
import com.flappy.smartdiff.util.tcp.sdk.DataServiceManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timerTask

/**
 * @FileName: DiffPresenter
 * @Author: luweiming
 * @Date: 2020/12/11 11:02
 * @Description:
 * @Version: 1.0
 */
class DiffPresenter : DiffContract.IDiffPresenter,
    BasePresenter<DiffContract.IDiffView, DiffModel>() {
    private var pihao_IP by Preference(Constant.KEY_PIHAO_IP, "192.168.1.10")
    private val dp01 = byteArrayOf(
        0x02,
        0x30,
        0x31,
        0x34,
        0x37,
        0x30,
        0x32,
        0x44,
        0x52,
        0x30,
        0x31,
        0x30,
        0x30,
        0x34
    )
    private val dp02 = byteArrayOf(
        0x02,
        0x30,
        0x31,
        0x34,
        0x37,
        0x30,
        0x32,
        0x44,
        0x52,
        0x30,
        0x31,
        0x31,
        0x30,
        0x34
    )
    private val dp03 = byteArrayOf(
        0x02,
        0x30,
        0x31,
        0x34,
        0x37,
        0x30,
        0x32,
        0x44,
        0x52,
        0x30,
        0x31,
        0x32,
        0x30,
        0x34
    )
    private val dp04 = byteArrayOf(
        0x02,
        0x30,
        0x31,
        0x34,
        0x37,
        0x30,
        0x32,
        0x44,
        0x52,
        0x30,
        0x31,
        0x33,
        0x30,
        0x34
    )
    private val dp05 = byteArrayOf(
        0x02,
        0x30,
        0x31,
        0x34,
        0x37,
        0x30,
        0x32,
        0x44,
        0x52,
        0x30,
        0x31,
        0x34,
        0x30,
        0x34
    )
    private val dp06 = byteArrayOf(
        0x02,
        0x30,
        0x31,
        0x34,
        0x37,
        0x30,
        0x32,
        0x44,
        0x52,
        0x30,
        0x31,
        0x35,
        0x30,
        0x34
    )
    private val dp07 = byteArrayOf(
        0x02,
        0x30,
        0x31,
        0x34,
        0x37,
        0x30,
        0x32,
        0x44,
        0x52,
        0x30,
        0x31,
        0x36,
        0x30,
        0x34
    )
    private val dp08 = byteArrayOf(
        0x02,
        0x30,
        0x31,
        0x34,
        0x37,
        0x30,
        0x32,
        0x44,
        0x52,
        0x30,
        0x31,
        0x37,
        0x30,
        0x34
    )

    override fun getMaterials(): List<MaterialBean> {
        return mModel.getMaterial()
    }

    override fun openLock(index: Int) {

        Thread({
            var tryCount = 0
            val equip = JYDAMEquip()
            val ip by Preference(Constant.KEY_TCP_IP, "192.168.10.1")
            equip.Init(ip, 10000, 254)
            if (!equip.IsConnect()) {
                equip.BeginConnect()
                while (!equip.IsConnect()) {
                    Thread.sleep(200)
                    tryCount++
                    if (tryCount > 1) {
                        mView?.showToast("设备未连接")
                        return@Thread
                    }
                }
            }
            equip.writeSignalDO(index, 1)
            Thread.sleep(20)
            val value = equip.readSignalDO(index)
            if (1 == value) {
                mView?.openSucc()
            } else {
                mView?.showToast("开启失败")
            }
            Thread.sleep(1000)
            equip.writeSignalDO(index, 0)
            equip.DisConnect()

        }).start()
    }

    override fun sendPihao(index: Int, pihao: String) {
        val timer = Timer()
        timer.schedule(timerTask {
            mView?.showToast("批号回传失败")
        },10000)
        var serverModel = ServerModel(pihao_IP, 500)
        DataServiceManager.getInstance().setServerModel(serverModel)
        DataServiceManager.getInstance().addObserver(fun(ip: String, data: ByteArray) {
            timer.cancel()
            //去除首位标志位
            var result= String(data.copyOfRange(1,data.size-1))
            if(result.length>=3&&result.get(result.length-3)=='0'){
                mView?.showToast("批号回传成功")
            }else{
                mView?.showToast("批号回传失败")
            }
            DataServiceManager.getInstance().stop(mView?.getMyContext());

        })
        DataServiceManager.getInstance().setOnSessionListener(object : OnSessionListener {
            override fun connected() {
                val packetBuffer = PacketBuffer()
                packetBuffer.data = getDataForIndex(index, pihao)
                DataServiceManager.getInstance().sendTcp(packetBuffer)
            }

            override fun disconnect(error: String?) {
            }

        })
        DataServiceManager.getInstance().start(mView?.getMyContext())
    }

    private fun getDataForIndex(index: Int, no: String): ByteArray? {
        var start = byteArrayOf()
        when (index) {
            0 -> start = dp01
            1 -> start = dp02
            2 -> start = dp03
            3 -> start = dp04
            4 -> start = dp05
            5 -> start = dp06
            6 -> start = dp07
            7 -> start = dp08
        }
        val formater = SimpleDateFormat("MMddHHmm")
        val timeString: String = formater.format(Date())
        var pihao = no
        while (pihao.length<8){
            pihao = "0".plus(pihao)
        }
        val timeBytes: ByteArray = timeString.toByteArray()
        val pihaoBytes = pihao.toByteArray()
        var sum = 0
        for (it in start) {
            sum += it
        }
        for (it in pihaoBytes) {
            sum += it
        }
        for (it in timeBytes) {
            sum += it
        }
        val lrc = sum.and(0xff).toString(16).toUpperCase().toByteArray()
        return start.plus(pihaoBytes).plus(timeBytes).plus(lrc).plus(0x03)

    }

    override fun createModel(): DiffModel {
        return DiffModel()
    }
}
