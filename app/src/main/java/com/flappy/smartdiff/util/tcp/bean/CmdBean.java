package com.flappy.smartdiff.util.tcp.bean;

import java.io.Serializable;

public class CmdBean implements Serializable {
    private String cmd;
    private String mac;
    private String ttl;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getTtl() {
        return ttl;
    }

    public void setTtl(String ttl) {
        this.ttl = ttl;
    }

    @Override
    public String toString() {
        return "CmdBean{" +
                "cmd='" + cmd + '\'' +
                ", mac='" + mac + '\'' +
                ", ttl='" + ttl + '\'' +
                '}';
    }
}
