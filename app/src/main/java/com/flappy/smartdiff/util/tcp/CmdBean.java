package com.flappy.smartdiff.util.tcp;

import java.io.Serializable;

public class CmdBean implements Serializable {

    private String cmd;
    private String mac;
    private long ttl;

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

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }
}
