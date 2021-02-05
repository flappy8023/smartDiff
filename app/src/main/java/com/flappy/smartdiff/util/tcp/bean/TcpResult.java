package com.flappy.smartdiff.util.tcp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * tcp协议返回的数据
 */
public class TcpResult implements Serializable {
    private String result;
    private List<DevBean> dev;
    private List<DevBean> sta;
    private String sequence;
    private String deviceId;
    private String MAC;
    private int step;

    @Override
    public String toString() {
        return "TcpResult{" +
                "result='" + result + '\'' +
                ", dev=" + dev +
                ", sta=" + sta +
                ", sequence='" + sequence + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", MAC='" + MAC + '\'' +
                ", step=" + step +
                '}';
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getResult() {
        return result;
    }

    public List<DevBean> getSta() {
        return sta;
    }

    public void setSta(List<DevBean> sta) {
        this.sta = sta;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<DevBean> getDev() {
        return dev;
    }

    public void setDev(List<DevBean> dev) {
        this.dev = dev;
    }

}
