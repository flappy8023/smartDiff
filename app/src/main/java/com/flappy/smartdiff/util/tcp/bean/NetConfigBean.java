package com.flappy.smartdiff.util.tcp.bean;

/**
 * @ClassName: NetConfigBean
 * @Description: java类作用描述
 * @Author: luweiming
 * @CreateDate: 2020/1/9 16:44
 * @Version: 1.0
 */
public class NetConfigBean {
    private String type;
    private int sequence;
    private String result;
    private NetWifi wifi;


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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public NetWifi getWifi() {
        return wifi;
    }

    public void setWifi(NetWifi wifi) {
        this.wifi = wifi;
    }

    @Override
    public String toString() {
        return "NetConfigBean{" +
                "type='" + type + '\'' +
                ", sequence='" + sequence + '\'' +
                ", result='" + result + '\'' +
                ", wifi=" + wifi +
                '}';
    }
}
