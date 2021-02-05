package com.flappy.smartdiff.util.udp;


import android.util.Log;

import com.flappy.smartdiff.util.tcp.PacketBuffer;

import java.net.DatagramSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class BaseThread extends Thread {
    private static final String TAG = "BaseThread";
    protected static boolean runnable = true;
    protected static BlockingQueue<PacketBuffer> inQueue = new LinkedBlockingQueue<PacketBuffer>();
    protected static BlockingQueue<PacketBuffer> outQueue = new LinkedBlockingQueue<PacketBuffer>();
    protected DatagramSocket datagramSocket;
    protected byte[] buffer = new byte[8192];

    public void close() {
        runnable = false;
        if (datagramSocket != null && datagramSocket.isBound()
                && datagramSocket.isClosed() == false) {
            datagramSocket.close();
        }
        interrupt();
    }

    public void dispatch(PacketBuffer packet) {
        if (packet == null)
            return;
        byte[] data = packet.getData();
        if (data == null || data.length == 0)
            return;
        postData(packet);
    }

    private void postData(PacketBuffer packet) {
        if (packet != null && packet.getData() != null) {
            boolean b = inQueue.offer(packet);
            if (!b) {
                Log.e(TAG,"this packet is loss:" + packet.toString());
            }
        }
    }

}
