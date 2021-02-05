package com.flappy.smartdiff.util.udp;



import android.util.Log;

import com.flappy.smartdiff.util.tcp.ByteUtils;
import com.flappy.smartdiff.util.tcp.PacketBuffer;
import com.flappy.smartdiff.util.tcp.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClient extends BaseThread {
    public UdpClient(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
        setName("UdpClient" + Utils.getCurTime());
    }

    @Override
    public void close() {
        super.close();
        runnable = false;
    }

    public static void main(String[] args) {
        String ip = "192.168.10.113";
        String ip1 = "192.168.10.255";
        String[] aa = ip.split("\\.");
        int len = aa[3].length();
        if (len == 1) {
            ip += "  ";
        } else if (len == 2) {
            ip += " ";
        }
        System.out.println(ip + "aaaa\r\n" + ip1);
    }

    private static final String TAG = "UdpClient";
    @Override
    public void run() {
        super.run();
        while (runnable) {
            try {
                //若队列为空，则线程阻塞在此处
                PacketBuffer data = outQueue.take();
                send(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG,"udpClient thread close");
    }

    public void send(byte[] data, String ip, int port) {
        PacketBuffer packet = new PacketBuffer();
        packet.setData(data);
        packet.setPort(port);
        packet.setIp(ip);
        if (data != null) {
            packet.setLength(data.length);
        }
        putData(packet);
    }

    private void send(PacketBuffer packetBuffer) throws IOException {
        if (datagramSocket == null) {
            throw new IOException("datagramSocket is null");
        }
        if (packetBuffer == null) {
            throw new IOException("packetBuffer is null");
        }
        byte[] bytes = packetBuffer.getData();
        if (bytes == null || bytes.length == 0) {
            throw new IOException("bytes is null or size is zero");
        }
        String ip = packetBuffer.getIp();
        if (ByteUtils.isNull(ip)) {
            throw new IOException("ip is null");
        }
        int port = packetBuffer.getPort();
        if (port == 0) {
            throw new IOException("port is zero");
        }
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(ip), port);

    }

    public void putData(PacketBuffer packet) {
        //若队列已经满，则等待有空间再继续。
        boolean b = outQueue.offer(packet);
        if (!b) {
            Log.e(TAG,"this packet offer faile:" + packet.toString());
        }

    }
}
