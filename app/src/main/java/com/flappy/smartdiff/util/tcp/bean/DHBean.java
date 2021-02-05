package com.flappy.smartdiff.util.tcp.bean;

/**
 * @ClassName: DHBean
 * @Description: java类作用描述
 * @Author: luweiming
 * @CreateDate: 2019/12/26 17:30
 * @Version: 1.0
 */
public class DHBean extends BaseBean {
    private DHDataBean data;

    public DHDataBean getData() {
        return data;
    }

    public void setData(DHDataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DHBean{" +
                "data=" + data +
                "} " + super.toString();
    }
}
