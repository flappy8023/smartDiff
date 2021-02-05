package com.flappy.smartdiff.util.tcp.bean;

import java.io.Serializable;

public class OpenKLBean implements Serializable {

    private String cmd;
    private String enable;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "OpenKLBean{" +
                "cmd='" + cmd + '\'' +
                ", enable='" + enable + '\'' +
                '}';
    }
}
