package com.flappy.smartdiff.util.tcp.bean;

/**
 * @ClassName: KeyModeBean
 * @Description: java类作用描述
 * @Author: luweiming
 * @CreateDate: 2019/12/26 17:20
 * @Version: 1.0
 */
public class KeyModeBean {
    private String keymode;

    public String getKeymode() {
        return keymode;
    }

    public void setKeymode(String keymode) {
        this.keymode = keymode;
    }

    @Override
    public String toString() {
        return "KeyModeBean{" +
                "keymode='" + keymode + '\'' +
                '}';
    }
}
