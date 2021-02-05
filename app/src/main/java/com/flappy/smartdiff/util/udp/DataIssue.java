package com.flappy.smartdiff.util.udp;


import android.util.Log;

import com.flappy.smartdiff.util.tcp.ByteUtils;
import com.flappy.smartdiff.util.tcp.PacketBuffer;
import com.flappy.smartdiff.util.tcp.Utils;
import com.flappy.smartdiff.util.tcp.listener.IRecevie;

public class DataIssue extends BaseThread {
    private static final String TAG = "DataIssue";
    private static IRecevie callback;

    public DataIssue() {
        setName("DataIssue-" + Utils.getCurTime());
    }

    @Override
    public void close() {
        runnable = false;
        interrupt();
    }

    @Override
    public void run() {
        super.run();
        while (runnable) {
            try {
                //从队列中取数据，如果队列为空，则阻塞在此处
                PacketBuffer packet = inQueue.take();
                //Log.w("出->" + inQueue.size());
                recv(packet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.e(TAG,"DataIssue thread close");
    }

    private void recv(PacketBuffer packet) throws Exception {
        if (packet == null)
            throw new Exception("packet is null...");
        byte[] data = packet.getData();
        if (data == null || data.length == 0)
            throw new Exception("data is null or length is zero...");
        String ip = packet.getIp();
        byte[] recv = data;//checkData(data, len, ip);
        if (recv != null) {
            packet.setData(recv);
            if (callback != null) {
                callback.onRecevie(packet);
            }

        }
    }

    /**
     * 设置数据回调
     *
     * @param call 回调函数
     */
    public void setCallback(IRecevie call) {
        callback = call;
    }
}
