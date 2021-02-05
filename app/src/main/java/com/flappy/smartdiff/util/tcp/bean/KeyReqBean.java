package com.flappy.smartdiff.util.tcp.bean;

import java.util.List;

/**
 * @ClassName: KeyReqBean
 * @Description: java类作用描述
 * @Author: luweiming
 * @CreateDate: 2019/12/26 17:12
 * @Version: 1.0
 */
public class KeyReqBean extends BaseBean {
    private String version;
    private List<KeyModeBean> keymodelist;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<KeyModeBean> getKeymodelist() {
        return keymodelist;
    }

    public void setKeymodelist(List<KeyModeBean> keymodelist) {
        this.keymodelist = keymodelist;
    }

    @Override
    public String toString() {
        return "KeyReqBean{" +
                "version='" + version + '\'' +
                ", keymodelist=" + keymodelist +
                "} " + super.toString();
    }
}
