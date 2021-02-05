package com.flappy.smartdiff.util.tcp;


import android.util.Log;

import com.flappy.smartdiff.util.tcp.listener.IRecevie;
import com.flappy.smartdiff.util.tcp.listener.OnSessionListener;
import com.flappy.smartdiff.util.tcp.listener.OnTransListener;
import com.flappy.smartdiff.util.tcp.listener.RspHandler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NioTcpClient extends Thread {
    private static final String TAG = "NioTcpClient";
    protected boolean runnable = true;
    private NioClient nioClient;
    private BlockingQueue<PacketBuffer> dataQueue = new LinkedBlockingQueue<PacketBuffer>();
    private String ip;
    private int port;
    private static IRecevie callback;
    private OnSessionListener onSessionListener;

    public NioTcpClient() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        System.out.println(sf.format(new Date()));
        setName("TcpClient" + sf.format(new Date()));
        setDaemon(true);
        create();
    }

    public void close() {
        runnable = false;
        if (nioClient != null) {
            nioClient.close();
        }
    }


    public void setTimeOut(int timeOut) {
        if (nioClient != null) {
            nioClient.setTimeOut(timeOut);
        }
    }

    @Override
    public void run() {
        super.run();
        while (runnable) {
            try {
                PacketBuffer data = dataQueue.take();
                write(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "NioTcp thread close!!!");
    }

    public void setOnSessionListener(OnSessionListener onSessionListener) {
        this.onSessionListener = onSessionListener;
    }

    private RspHandler rspHandler = new RspHandler() {
        @Override
        public void onConnect(SelectionKey session) {
            if (onSessionListener != null) {
                onSessionListener.connected();
            }
//            Log.e(TAG, "onConnect~~:" + session.toString());
        }

        @Override
        public void onDisconnect(SelectionKey session, Throwable cause) {
            String error = (cause == null ? "ssessoin closed" : cause.getMessage());
            if (onSessionListener != null) {
                onSessionListener.disconnect(error);
            }
//            Log.e(TAG, onSessionListener + "~~~~~onDisconnect:" + session.toString() + " " + error);
        }

        @Override
        public void onConnectfailed(int id, SelectionKey session, Throwable cause) {
            if (onSessionListener != null) {
                onSessionListener.disconnect(cause == null ? "ssessoin closed" : cause.getMessage());
            }

            if (session != null)
                Log.e(TAG, "onConnectfailed:");
        }

        @Override
        public void onWriteError(int id, SelectionKey session, Throwable cause) {
            Log.e(TAG, "onWriteError:id = " + id);
        }

        @Override
        public boolean onRecevie(Object message) {
//            Log.e("this packet ######:" + new String((byte[]) message));
            byte[] data = (byte[]) message;
            //dumpStack();
            if (data != null) {
//                Log.e("tcp.messageReceived:" + ByteUtils.toHexString(data));
                PacketBuffer packet = new PacketBuffer();
                packet.setLength(data.length);
                packet.setIp("");
                packet.setData(data);
                //-------------------处理数据-----------------
//                dispatch(packet);
                //回调到上层
                Log.e(TAG, "tcp recevie data:" + ByteUtils.toHexStringHexo(data));
                if (callback != null) {
                    callback.onRecevie(packet);
                }
            }
            return false;
        }

    };

    public void send(PacketBuffer packet) {
        reconnect();
        boolean b = dataQueue.offer(packet);
        if (!b) {
            Log.e(TAG, "this packet offer faile:" + packet.toString());
        }
    }

    private void write(PacketBuffer data) {
//        Log.i("uu.tcp.send.write.runnable :" + runnable + " " + nioClient + " size:" + data.getData().length + "  " + ByteUtils.toHexString(data.getData()));
        reconnect();
        if (nioClient != null) {
            try {
                if (nioClient.isChannelConnect()) {
//                    Log.e("TCP is connected...");
                } else {
                    Log.e(TAG, "TCP is not connected...");
                    if (onSessionListener != null) {
                        onSessionListener.disconnect("TCP is not connected...");
                    }
                }
//                if (!nioClient.isChannelConnect()){
//                    nioClient.open();
//                }
                nioClient.send(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //open tcp
    private void create() {
        if (nioClient == null) {
            nioClient = new NioClient();
        }
        //Log.w("#################create");
        nioClient.setListener(rspHandler);
    }

    public void open(final String ip, final int port, final OnTransListener listener) {
        this.ip = ip;
        this.port = port;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    rspHandler.setOnTransListener(listener);
                    nioClient.open(ip, port);
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onFailed(e);
                    }
                }
            }
        }, "opentcp").start();
    }

    public boolean isChannelConnect() {
        if (nioClient == null) {
            return false;
        }
        boolean b = nioClient.isChannelConnect();
        return b;
    }

    private void reconnect() {
        if (!runnable) {
            runnable = true;
            create();
        }
    }

    public void setCallBack(IRecevie callback) {
        this.callback = callback;
    }
}
