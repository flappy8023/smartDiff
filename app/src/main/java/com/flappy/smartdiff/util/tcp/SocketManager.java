package com.flappy.smartdiff.util.tcp;


import android.util.Log;

import com.flappy.smartdiff.util.tcp.listener.IRecevie;
import com.flappy.smartdiff.util.tcp.listener.OnSessionListener;
import com.flappy.smartdiff.util.udp.DataIssue;
import com.flappy.smartdiff.util.udp.UdpClient;
import com.flappy.smartdiff.util.udp.UdpServer;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class SocketManager {
    private static final String TAG = "SocketManager";
    public static final int MINIMUM_DELAY = 0x10;
    private static SocketManager instance = null;
    //设置缓冲区大小
    private static int SO_BUFSIZE = 8192;
    //端口复用
    private static boolean SO_REUSEADDR = true;
    //设置广播
    private static boolean SO_BROADCAST = true;

    private NioTcpClient tcpClient;

    private DatagramSocket datagramSocket;
    private UdpServer server;
    private UdpClient client;
    private DataIssue udpBiz;

    private int trafficType = MINIMUM_DELAY;

    public SocketManager() {
    }

    public void setCallBack(IRecevie callback) {
        if (tcpClient != null) {
            tcpClient.setCallBack(callback);
        }
        if (udpBiz != null) {
            udpBiz.setCallback(callback);
        }
    }

    public static SocketManager getInstance() {
        if (null == instance) {
            synchronized (SocketManager.class) {
                instance = new SocketManager();
            }
        }
        return instance;
    }

    public void setOnSessionListener(OnSessionListener onSessionListener) {
        if (tcpClient != null) {
            tcpClient.setOnSessionListener(onSessionListener);
        }
    }

    public void createDataIssue() {
        if (udpBiz == null) {
            udpBiz = new DataIssue();
            udpBiz.start();
        }
    }

    public void createTcpServer() {
        if (tcpClient == null) {
            tcpClient = new NioTcpClient();
            tcpClient.start();
        }

    }

    public void createUpdServer(int port) {
        try {
            if (createSocket(port)) {
                client = new UdpClient(datagramSocket);
                server = new UdpServer(datagramSocket);
                client.start();
                server.start();
            } else {
                throw new SocketException("socket create failed...");
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public NioTcpClient getTcpClient() {
        if (tcpClient == null)
            throw new IllegalArgumentException("tcpclient is null");
        return tcpClient;
    }

    public UdpClient getUdpClient() {
        if (client == null)
            throw new IllegalArgumentException("udpclient is null");
        return client;
    }


    private boolean createSocket(int port) throws SocketException {
        datagramSocket = new DatagramSocket(null);
        datagramSocket.setBroadcast(SO_BROADCAST);
        datagramSocket.setReceiveBufferSize(SO_BUFSIZE);
        datagramSocket.setTrafficClass(trafficType);
        datagramSocket.setReuseAddress(SO_REUSEADDR);
        datagramSocket.bind(new InetSocketAddress(port));
        if (datagramSocket != null) {
            return true;
        }
        return false;
    }

    public void close() {
        Log.e(TAG, "关闭Scoket：：Socket close!!!");
        if (datagramSocket != null) {
            if (!datagramSocket.isClosed()) {
                datagramSocket.close();
            }
            datagramSocket.disconnect();
        }

//        if (server != null) {
//            server.close();
//            server = null;
//        }
        if (udpBiz != null) {
            udpBiz.close();
            udpBiz = null;
        }
//        if (client != null) {
//            client.close();
//            client = null;
//        }

        if (tcpClient != null) {
            tcpClient.close();
            tcpClient = null;
        }
    }
}
