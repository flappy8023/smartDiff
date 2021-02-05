package com.flappy.smartdiff.util.tcp.listener;

public interface ITcpDataListener {
    /**
     * tcp返回数据
     *
     * @param ip   ip地址
     * @param data 数据
     */
    void recevie(String ip, byte[] data);
}
