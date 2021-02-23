package com.flappy.smartdiff.util.tcp.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.flappy.smartdiff.util.tcp.ByteUtils;
import com.flappy.smartdiff.util.tcp.PacketBuffer;
import com.flappy.smartdiff.util.tcp.TCPConnectManager;
import com.flappy.smartdiff.util.tcp.Utils;
import com.flappy.smartdiff.util.tcp.bean.ServerModel;
import com.flappy.smartdiff.util.tcp.listener.ITcpDataListener;
import com.flappy.smartdiff.util.tcp.listener.OnSessionListener;

import java.util.ArrayList;
import java.util.List;

public class DataServiceManager {
    private static final String TAG = "DataServiceManager";
    private static DataServiceManager instance;
    private Context context;
    private final Messenger mMessenger = new Messenger(new IncomingHandler());
    private Messenger mService = null;
    private boolean mIsBound;

    //在线离线的回调
    private OnSessionListener onSessionListener;
    public static boolean bIsInit = false;

    private TCPConnectManager connectManager;
    private ServerModel serverModel;

    public void setServerModel(ServerModel serverModel) {
        this.serverModel = serverModel;
    }

    public ServerModel getServerMode() {
        if (this.serverModel != null && !TextUtils.isEmpty(serverModel.getIp()) && serverModel.getPort() != 0) {
            return serverModel;
        }
        //默认ip 端口
        serverModel = new ServerModel("61.141.158.190", 9000);
        return serverModel;
    }

    public static DataServiceManager getInstance() {
        if (instance == null) {
            synchronized (DataServiceManager.class) {
                if (instance == null) {

                    instance = new DataServiceManager();
                }
            }
        }
        return instance;
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            try {
                Message msg = Message.obtain(null,
                        DataService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
            } catch (RemoteException e) {
                Log.i(TAG, "onServiceConnected.." + e.getMessage());
            }

            //Service init complete
            getTCPConnectLogic().startLogic(getServerMode());
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "与TcpService的连接断开：Disconnected from remote service mService:" + mService + " context:" + context);
            bIsInit = false;
            if (context != null) {
                try {
                    Log.i(TAG, "开始重新启动TcpService");
                    start(context);
                } catch (Exception e) {
                    Log.e(TAG, "重新启动TcpService失败：" + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }
    };


    //重置
    public void reset() {
        reset(2000);
    }

    public void reset(long time) {
        if (time <= 0) {
            time = 100;
        }
        bIsInit = false;
        try {
            stop(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                start(context);
            }
        }, time);
    }

    public void start(Context c) {
        synchronized (DataServiceManager.class) {
            if (!bIsInit) {
                bIsInit = true;
                if (c == null) {
                    return;
                }
                this.context = c;

                Intent intent = new Intent();
                int androidVersion = Utils.getSDKVersionNumber();
                Log.e(TAG, "cucent android os version:" + androidVersion + " model=" + android.os.Build.MODEL + " thread:" + Thread.currentThread().getName());
                if (androidVersion >= 21 || "MI PAD".equalsIgnoreCase(android.os.Build.MODEL)) {
                    //只一句至关重要，对于android5.0以上，所以minSdkVersion最好小于21；
                    intent.setPackage(context.getPackageName());
                }
                String serviceDemonName = "com.flappy.smartdiff.action.DataService";
                intent.setAction(serviceDemonName);
                mIsBound = context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                if (!mIsBound) {
                    Log.e(TAG, "初始化TcpService失败...");
                } else {
                    Log.i(TAG, "成功初始化TcpService App.packageName=" + context.getPackageName() + " mService=" + mService + " mConnection=" + mConnection);
                }

            }
        }
    }


    //获取TCP实例
    public TCPConnectManager getTCPConnectLogic() {
        if (this.connectManager == null) {
            this.connectManager = new TCPConnectManager();
        }
        return connectManager;
    }


    public void stop(Context context) throws Exception {
        bIsInit = false;
        ByteUtils.frameNumber = 1;
        if (mIsBound) {
            closeTcp();

            if (mService != null) {
                Message msg = Message.obtain(null,
                        DataService.MSG_UNREGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
            }

            // Detach our existing connection.
            context.unbindService(mConnection);
            mIsBound = false;
            Log.i(TAG, "uu结束Service绑定");
        }

        if (connectManager != null) {
            connectManager.release();
        }
    }

    /**
     * Handler of incoming messages from service.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataService.MSG_RECV:
                    recv(msg);
                    break;
                case DataService.MSG_SUB_TCP_CONNECTED:
                case DataService.MSG_SUB_TCP_DISCONNECTED:
                    processChannel(msg);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    /**
     * 返回TCP状态
     *
     * @param msg 数据
     */
    private void processChannel(Message msg) {
        if (msg == null || msg.obj == null) {
            return;
        }
        Bundle bundle = (Bundle) msg.obj;
        if (bundle == null) {
            return;
        }
        String data = bundle.getString(DataService.MSG_ARG_DATA);
        Log.e(TAG, "uuuutcp.error.cmd:" + msg.what + " msg:" + data);
        if (msg.what == DataService.MSG_SUB_TCP_CONNECTED) {//tcp 连接上
            Log.i(TAG, "tcp 连接成功");
            //tcp ---连接成功
            if (onSessionListener != null) {
                onSessionListener.connected();
            }
            //连接成功 关闭tcp接口
            connectManager.switchStep(TCPConnectManager.BizStep.OVER, "connected");
        } else if (msg.what == DataService.MSG_SUB_TCP_DISCONNECTED) {//TCP断开
            //tcp断开后计数器要重置
            ByteUtils.frameNumber = 1;
            //tcp ---断开
            if (onSessionListener != null) {
                onSessionListener.disconnect(data);
            }
            Log.i(TAG, "tcp 连接断开，开始重新连接tcp");
            //收到tcp断开的连接  重连TCP连接
            Log.e(TAG, "uuu in TCPconnect tcp DISCONNECT:");
            connectManager.switchStep(TCPConnectManager.BizStep.RECONNECTTCP, "disconnect");
        }
    }

    /**
     * 设置在线离线的回调
     *
     * @param onSessionListener 回调函数
     */
    public void setOnSessionListener(OnSessionListener onSessionListener) {
        this.onSessionListener = onSessionListener;
    }

    /**
     * tcp返回的原始数据
     *
     * @param msg data
     */
    private void recv(Message msg) {
        if (msg == null || msg.obj == null) {
            return;
        }
        Bundle bundle = (Bundle) msg.obj;
        if (bundle == null) {
            return;
        }
        byte[] data = bundle.getByteArray(DataService.MSG_ARG_DATA);
        if (data == null || data.length == 0) {
            return;
        }
        byte packetStart = data[0];
        //数据来源IP地址
        String ip = bundle.getString(DataService.MSG_ARG_IP);
        notifyObservers(ip, data);
    }


    private static List<ITcpDataListener> observers = new ArrayList<>();

    public synchronized void addObserver(ITcpDataListener observer) {
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public synchronized void deleteObserver(ITcpDataListener observer) {
        observers.remove(observer);
    }

    public synchronized void deleteObservers() {
        observers.clear();
    }

    public void notifyObservers(String ip, byte[] data) {
        int size = 0;
        ITcpDataListener[] arrays = null;
        synchronized (this) {
            size = observers.size();
            arrays = new ITcpDataListener[size];
            observers.toArray(arrays);
        }
        if (arrays != null) {
            for (ITcpDataListener observer : arrays) {
                observer.recevie(ip, data);
            }
        }
    }


    public void openSocket(String ip, int port) {
        try {
            Message msg = Message.obtain(null,
                    DataService.MSG_INIT_SOCKET);
            msg.replyTo = mMessenger;
            Bundle bundle = new Bundle();
            bundle.putString(DataService.MSG_ARG_IP, ip);
            bundle.putInt(DataService.MSG_ARG_PORT, port);
            msg.obj = bundle;
            if (mService != null) {
                mService.send(msg);
            } else {
                Log.w(TAG, "udpService being initialized,so wait " + context);
            }
        } catch (RemoteException e) {
            Log.i(TAG, "openSocket fail.." + e.getMessage());
        }
    }


    public void sendTcp(PacketBuffer data) throws Exception {
        if (data != null) {
            data.setSubCmd(DataService.MSG_SUB_TCP);
            send(data);
        }
    }


    private void send(PacketBuffer data) throws Exception {
        if (data == null) {
            return;
        }
        if (data.getData() != null && data.getData().length > 0) {
            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString(DataService.MSG_ARG_IP, data.getIp());
            bundle.putInt(DataService.MSG_ARG_PORT, data.getPort());
            bundle.putByteArray(DataService.MSG_ARG_DATA, data.getData());
            msg.what = DataService.MSG_SEND;
            msg.arg1 = data.getSubCmd();
            msg.obj = bundle;
            if (mService != null) {
                mService.send(msg);
            } else {
                Log.w(TAG, "udpService being initialized,so wait " + context);
            }
        }
    }

    /**
     * udp 发送
     *
     * @param data
     */
    public void sendUdp(PacketBuffer data) throws Exception {
        if (data != null) {
            data.setSubCmd(DataService.MSG_SUB_UDP);
            send(data);
        }
    }

    public void openAndSendTcp(PacketBuffer data) throws Exception {
        if (data == null) {
            return;
        }
        if (data.getData() != null && data.getData().length > 0) {
            Message msg = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString(DataService.MSG_ARG_IP, data.getIp());
            bundle.putInt(DataService.MSG_ARG_PORT, data.getPort());
            bundle.putByteArray(DataService.MSG_ARG_DATA, data.getData());
            msg.what = DataService.MSG_INIT_SOCKET;
            msg.arg1 = data.getSubCmd();
            msg.obj = bundle;
            if (mService != null) {
                mService.send(msg);
            } else {
                Log.w(TAG, "udpService being initialized,so wait " + context);
            }
        }
    }

    public void closeTcp() throws Exception {
        Message msg = Message.obtain();
        msg.what = DataService.MSG_UNINIT_SOCKET;
        if (mService != null) {
            mService.send(msg);
        } else {
            Log.w(TAG, "udpService being initialized,so wait " + context);
        }
    }

}