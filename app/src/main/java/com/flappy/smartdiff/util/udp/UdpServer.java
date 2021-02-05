package com.flappy.smartdiff.util.udp;



import android.util.Log;

import com.flappy.smartdiff.util.tcp.PacketBuffer;
import com.flappy.smartdiff.util.tcp.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;


public class UdpServer extends BaseThread {
    protected DatagramPacket datagramPacket;

    public UdpServer(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
        setName("UdpServer" + Utils.getCurTime());
    }

    @Override
    public void run() {
        super.run();
        datagramPacket = new DatagramPacket(buffer, buffer.length);
        while (runnable) {
            try {
                datagramSocket.receive(datagramPacket);
                PacketBuffer packet = new PacketBuffer();
                packet.setLength(datagramPacket.getLength());
                packet.setPort(datagramPacket.getPort());
                packet.setIp(datagramPacket.getAddress().getHostAddress().toString());
//                Logc.i("接收队列大小->" + inQueue.size() + " " + ByteUtils.toHexString(packet.getData()));//+""+ ByteUtils.toHexString(datagramPacket.getData()));
                byte[] remo = new byte[datagramPacket.getLength()];
                System.arraycopy(datagramPacket.getData(), 0, remo, 0, datagramPacket.getLength());
                packet.setData(remo);
                dispatch(packet);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                Log.i(TAG,"UDPReceiver.start.run_SocketTimeoutException" + e.getMessage());
            } catch (IOException e) {
                Log.i(TAG,"UDPReceiver.start.run_IOException" + e.getMessage());
                e.printStackTrace();
            }
        }
        Log.e(TAG,"UdpService thread close");
    }

    private static final String TAG = "UdpServer";
}
