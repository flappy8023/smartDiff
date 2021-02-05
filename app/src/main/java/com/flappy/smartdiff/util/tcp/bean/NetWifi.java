package com.flappy.smartdiff.util.tcp.bean;

/**
 * @ClassName: NetWifi
 * @Description: java类作用描述
 * @Author: luweiming
 * @CreateDate: 2020/1/9 16:53
 * @Version: 1.0
 */
public class NetWifi {
    private String ssid;
    private String password;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "NetWifi{" +
                "ssid='" + ssid + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}