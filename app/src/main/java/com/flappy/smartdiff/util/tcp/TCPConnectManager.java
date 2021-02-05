package com.flappy.smartdiff.util.tcp;


import android.util.Log;

import com.flappy.smartdiff.util.tcp.bean.ServerModel;
import com.flappy.smartdiff.util.tcp.sdk.DataServiceManager;

public class TCPConnectManager {
    private static final String TAG = "TCPConnectManager";
    private byte[] lock = new byte[0];
    private Thread deamonThread = null;
    private BizStep step = BizStep.RECONNECTTCP;
    protected static ServerModel deviceServer;

    /**
     * 认证步骤
     */
    public enum BizStep {
        //请求认证密钥,上传运行数据,休眠
        RECONNECTTCP, OVER
    }

    private synchronized void createDeamonThread() {
        if (deamonThread == null) {
            deamonThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (lock) {
                        while (true) {
                            switch (step) {
                                case RECONNECTTCP:
                                    if (deviceServer != null) {
                                        //重联TCP
                                        Log.e(TAG, "开始重新连接Tcp.信息： " + deviceServer.getIp() + ":" + deviceServer.getPort());
                                        DataServiceManager.getInstance().openSocket(deviceServer.getIp(), deviceServer.getPort());
                                        try {
                                            lock.wait(10 * 1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                                case OVER:
                                    deamonThread = null;
                                    return;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }, "reconnect tcp-" + Utils.getCurTime());
            deamonThread.start();
        }
    }

    public void switchStep(final BizStep s, String name) {
        this.step = s;
        Log.e(TAG, "uuuuuuuuuuuu.switchStep:" + step);
        createDeamonThread();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        }, "switchStep-" + name + Utils.getCurTime()).start();
    }

    public TCPConnectManager() {
        createDeamonThread();
    }

    //开始连接tcp
    public void startLogic(Object obj) {
//        createDeamonThread();
        if (obj != null && obj instanceof ServerModel) {
            ServerModel serverModel = (ServerModel) obj;
            if (serverModel != null) {
                deviceServer = serverModel;
                switchStep(BizStep.RECONNECTTCP, "startLogic");
            }
        }
    }

    //释放
    public void release() {
        switchStep(BizStep.OVER, "over");
    }
}
