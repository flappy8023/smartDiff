package com.flappy.smartdiff.util.tcp.sdk;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.flappy.smartdiff.util.tcp.ByteUtils;
import com.flappy.smartdiff.util.tcp.PacketBuffer;
import com.flappy.smartdiff.util.tcp.SocketManager;
import com.flappy.smartdiff.util.tcp.Utils;
import com.flappy.smartdiff.util.tcp.listener.IRecevie;
import com.flappy.smartdiff.util.tcp.listener.OnSessionListener;
import com.flappy.smartdiff.util.tcp.listener.OnTransListener;

import java.util.ArrayList;

public class DataService extends Service implements IRecevie {
    private static final String TAG = "DataService";
    private static SocketManager socketManager = SocketManager.getInstance();
    private final Messenger mMessenger = new Messenger(new IncomingHandler());
    protected ArrayList<Messenger> mClients = new ArrayList<Messenger>();
    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_RECV = 3;
    public static final int MSG_SEND = 4;
    public static final int MSG_INIT_SOCKET = 5;
    public static final int MSG_UNINIT_SOCKET = 6;
    public static final int MSG_SUB_UDP = 7;
    public static final int MSG_SUB_TCP = 8;
    public static final int MSG_SUB_TCP_CONNECTED = 9;
    public static final int MSG_SUB_TCP_DISCONNECTED = 10;
    public static final String MSG_ARG_IP = "ip";
    public static final String MSG_ARG_PORT = "port";
    public static final String MSG_ARG_DATA = "data";

    private static int localport = 58899;

    public DataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "TcpService onCreate");
        socketManager = SocketManager.getInstance();
        socketManager.createTcpServer();
        socketManager.createUpdServer(localport);
        socketManager.createDataIssue();
        socketManager.setCallBack(this);
        socketManager.getTcpClient().setOnSessionListener(new OnSessionListener() {
            @Override
            public void connected() {
                sendMsgToClient(MSG_SUB_TCP_CONNECTED, "connect...");
            }

            @Override
            public void disconnect(String error) {
                sendMsgToClient(MSG_SUB_TCP_DISCONNECTED, error);
            }
        });
        socketManager.getTcpClient().setTimeOut(10 * 1000);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (socketManager != null) {
            socketManager.close();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }


    public void receive(String ip, byte[] data) {
        if (data != null && data.length > 0) {
            Message msg = Message.obtain();
            msg.what = MSG_RECV;
            Bundle recvData = new Bundle();
            recvData.putString(MSG_ARG_IP, ip);
            recvData.putByteArray(MSG_ARG_DATA, data);
            msg.obj = recvData;
            try {

                mMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.i(TAG, "Tcpservice接收消息失败：receive data failed:receive=" + e.getMessage());
            }
        }
    }

    private void sendMsgToClient(int cmd, String mess) {
        Message msg = Message.obtain();
        msg.what = cmd;
        Bundle recvData = new Bundle();
        recvData.putString(MSG_ARG_DATA, mess);
        msg.obj = recvData;
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
            Log.i(TAG, "receive data failed:receive=" + e.getMessage());
        }
    }

    @Override
    public void onRecevie(PacketBuffer packet) {
        if (packet == null) {
            return;
        }
        receive(packet.getIp(), packet.getData());
    }

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_SEND:
                    clientMsgDispatch(msg);
                    break;
                case MSG_INIT_SOCKET:
                    openTcp(msg);
                    break;
                case MSG_UNINIT_SOCKET:
                    closeTcp(msg);
                    break;
                case MSG_SUB_TCP_CONNECTED:
                case MSG_SUB_TCP_DISCONNECTED:
                case MSG_RECV:
                    for (int i = mClients.size() - 1; i >= 0; i--) {
                        try {
                            mClients.get(i).send(Message.obtain(null, msg.what, msg.obj));
                        } catch (RemoteException e) {
                            mClients.remove(i);
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private void openTcp(Message msg) {
        final PacketBuffer packet = getBundleData(msg);
        if (packet == null) {
            return;
        }
        socketManager.getTcpClient().open(packet.getIp(), packet.getPort(), new OnTransListener() {
            @Override
            public void onSucessfull() {
                Log.e(TAG, "Tcpservice  打开socket成功：openSocket onSucessfull, ip:" + packet.getIp() + ":" + packet.getPort() + " " + ByteUtils.toHexStringHexo(packet.getData()));
                if (packet.getData() != null) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            socketManager.getTcpClient().send(packet);
                        }
                    }).start();
                }
            }

            @Override
            public void onFailed(Object error) {
                Log.e(TAG, "打开Socket失败：openSocket onFailed: " + error + "  " + packet.toString());
            }
        });
    }

    private void closeTcp(Message msg) {
        socketManager.getTcpClient().close();
    }

    private void clientMsgDispatch(Message msg) {
        PacketBuffer packet = getBundleData(msg);
        if (packet == null) {
            return;
        }
        if (packet.getSubCmd() == MSG_SUB_TCP) {
            sendTcp(packet);
        } else if (packet.getSubCmd() == MSG_SUB_UDP) {
            sendUdp(packet);
        }
    }

    private void sendUdp(PacketBuffer packet) {
        if (packet.getData() == null) {
            return;
        }
        if (TextUtils.isEmpty(packet.getIp())) {
            packet.setIp(Utils.getBroadcastAddress(this));
        }
        if (packet.getPort() == 0) {
            packet.setPort(localport);
        }
        socketManager.getUdpClient().send(packet.getData(), packet.getIp(), packet.getPort());
    }

    private PacketBuffer getBundleData(Message msg) {
        if (msg == null) {
            return null;
        }
        if (msg.obj == null) {
            return null;
        }
        int subCmd = msg.arg1;
        Bundle bundle = (Bundle) msg.obj;
        if (bundle == null) {
            return null;
        }
        byte[] data = bundle.getByteArray(MSG_ARG_DATA);
        String ip = bundle.getString(MSG_ARG_IP);
        int port = bundle.getInt(MSG_ARG_PORT);
        return new PacketBuffer(data, ip, port, subCmd);
    }


    private void sendTcp(PacketBuffer packet) {
        if (packet.getData() == null) {
            return;
        }
        socketManager.getTcpClient().send(packet);
    }
}