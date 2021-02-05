package com.flappy.smartdiff.util.tcp.bean;

/**
 * @ClassName: BaseBean
 * @Description: java类作用描述
 * @Author: luweiming
 * @CreateDate: 2019/12/26 17:14
 * @Version: 1.0
 */
public class BaseBean {
    private String type;
    private int sequence;
    private String mac;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "type='" + type + '\'' +
                ", sequence=" + sequence +
                ", mac='" + mac + '\'' +
                '}';
    }
}
