package com.flappy.smartdiff.util.tcp.bean;

/**
 * @ClassName: NoticeBean
 * @Description: java类作用描述
 * @Author: luweiming
 * @CreateDate: 2020/1/10 9:42
 * @Version: 1.0
 */
public class NoticeBean {
    private String type;
    private String mac;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        return "NoticeBean{" +
                "type='" + type + '\'' +
                ", mac='" + mac + '\'' +
                '}';
    }
}
