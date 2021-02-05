package com.flappy.smartdiff.util.tcp.bean;

/**
 * @ClassName: DHDataBean
 * @Description: java类作用描述
 * @Author: luweiming
 * @CreateDate: 2019/12/26 17:30
 * @Version: 1.0
 */
public class DHDataBean {
    private String dh_key;
    private String dh_p;
    private String dh_g;

    public String getDh_key() {
        return dh_key;
    }

    public void setDh_key(String dh_key) {
        this.dh_key = dh_key;
    }

    public String getDh_p() {
        return dh_p;
    }

    public void setDh_p(String dh_p) {
        this.dh_p = dh_p;
    }

    public String getDh_g() {
        return dh_g;
    }

    public void setDh_g(String dh_g) {
        this.dh_g = dh_g;
    }

    @Override
    public String toString() {
        return "DHDataBean{" +
                "dh_key='" + dh_key + '\'' +
                ", dh_p='" + dh_p + '\'' +
                ", dh_g='" + dh_g + '\'' +
                '}';
    }
}
