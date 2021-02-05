package com.flappy.smartdiff.util.tcp.bean;

/**
 * @ClassName: DevRegDataBean
 * @Description: java类作用描述
 * @Author: luweiming
 * @CreateDate: 2019/12/26 17:35
 * @Version: 1.0
 */
public class DevRegDataBean {
    private String vendor;
    private String model;
    private String swversion;
    private String hdversion;
    private String sn;
    private String ipaddr;
    private String url;
    private String wireless;
    private String CTEI;
    private String devtype;
    private String description;

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSwversion() {
        return swversion;
    }

    public void setSwversion(String swversion) {
        this.swversion = swversion;
    }

    public String getHdversion() {
        return hdversion;
    }

    public void setHdversion(String hdversion) {
        this.hdversion = hdversion;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWireless() {
        return wireless;
    }

    public void setWireless(String wireless) {
        this.wireless = wireless;
    }

    public String getCTEI() {
        return CTEI;
    }

    public void setCTEI(String CTEI) {
        this.CTEI = CTEI;
    }

    public String getDevtype() {
        return devtype;
    }

    public void setDevtype(String devtype) {
        this.devtype = devtype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DevRegDataBean{" +
                "vendor='" + vendor + '\'' +
                ", model='" + model + '\'' +
                ", swversion='" + swversion + '\'' +
                ", hdversion='" + hdversion + '\'' +
                ", sn='" + sn + '\'' +
                ", ipaddr='" + ipaddr + '\'' +
                ", url='" + url + '\'' +
                ", wireless='" + wireless + '\'' +
                ", CTEI='" + CTEI + '\'' +
                ", devtype='" + devtype + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
