package com.flappy.smartdiff.util.tcp.listener;


import java.nio.channels.SelectionKey;

public abstract class RspHandler {

    //拒绝连接,基本是socketserver没启动
    public static int ECONN_REFUSED = 0x0100;
    //发送的时候服务器关闭
    public static int CHANNEL_CLOSED = 0x0200;
    //接收数据异常
    public static int READ_EXCEPTION = 0x0300;
    //写出数据异常
    public static int WRITE_EXCEPTION = 0x0400;
    //关闭通道异常
    public static int CLOSE_EXCEPTION = 0x0500;

    private OnTransListener onTransListener;

    public OnTransListener getOnTransListener() {
        return onTransListener;
    }

    public void setOnTransListener(OnTransListener onTransListener) {
        this.onTransListener = onTransListener;
    }

    public abstract void onConnect(SelectionKey session);

    public abstract void onDisconnect(SelectionKey session, Throwable cause);

    public abstract void onConnectfailed(int id, SelectionKey session, Throwable cause);

    public abstract void onWriteError(int id, SelectionKey session, Throwable cause);

    public abstract boolean onRecevie(Object message);
}
