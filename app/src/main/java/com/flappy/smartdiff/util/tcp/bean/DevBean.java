package com.flappy.smartdiff.util.tcp.bean;

import java.io.Serializable;

public class DevBean implements Serializable {

    private String mac;
    private String devid;
    private String key;
    private String expire;
    private boolean isSelected;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDevid() {
        return devid;
    }

    public void setDevid(String devid) {
        this.devid = devid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "DevBean{" +
                "mac='" + mac + '\'' +
                ", devid='" + devid + '\'' +
                ", key='" + key + '\'' +
                ", expire='" + expire + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
