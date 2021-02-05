package com.flappy.smartdiff.util.tcp.bean;

/**
 * @ClassName: DevRegBean
 * @Description: java类作用描述
 * @Author: luweiming
 * @CreateDate: 2019/12/26 17:35
 * @Version: 1.0
 */
public class DevRegBean extends BaseBean {
    private DevRegDataBean data;
    private String link_mac;

    public DevRegDataBean getData() {
        return data;
    }

    public void setData(DevRegDataBean data) {
        this.data = data;
    }

    public String getLink_mac() {
        return link_mac;
    }

    public void setLink_mac(String link_mac) {
        this.link_mac = link_mac;
    }

    @Override
    public String toString() {
        return "DevRegBean{" +
                "data=" + data +
                ", link_mac='" + link_mac + '\'' +
                "} " + super.toString();
    }
}
