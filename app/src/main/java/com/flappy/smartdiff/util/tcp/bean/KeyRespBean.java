package com.flappy.smartdiff.util.tcp.bean;

/**
 * @ClassName: KeyRespBean
 * @Description: java类作用描述
 * @Author: luweiming
 * @CreateDate: 2019/12/26 17:25
 * @Version: 1.0
 */
public class KeyRespBean extends BaseBean {
    private String keymode;

    public String getKeymode() {
        return keymode;
    }

    public void setKeymode(String keymode) {
        this.keymode = keymode;
    }

    @Override
    public String toString() {
        return "KeyRespBean{" +
                "keymode='" + keymode + '\'' +
                "} " + super.toString();
    }
}
