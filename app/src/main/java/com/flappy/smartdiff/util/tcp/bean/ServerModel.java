package com.flappy.smartdiff.util.tcp.bean;

import java.io.Serializable;

public class ServerModel implements Serializable {
    private String ip;
    private int port;

    //设备的mac
    private String mac;

    public ServerModel(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        return "ServerModelimplements{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", mac='" + mac + '\'' +
                '}';
    }
}
