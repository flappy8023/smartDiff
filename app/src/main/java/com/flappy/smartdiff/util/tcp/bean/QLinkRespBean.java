package com.flappy.smartdiff.util.tcp.bean;

import java.util.List;

/**
 * @ClassName: QLinkRespBean
 * @Description: java类作用描述
 * @Author: luweiming
 * @CreateDate: 2019/12/27 10:22
 * @Version: 1.0
 */
public class QLinkRespBean extends BaseBean {
    private String result;
    private List<WifiBean> wifi;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<WifiBean> getWifi() {
        return wifi;
    }

    public void setWifi(List<WifiBean> wifi) {
        this.wifi = wifi;
    }

    @Override
    public String toString() {
        return "QLinkRespBean{" +
                "result='" + result + '\'' +
                ", wifi=" + wifi +
                "} " + super.toString();
    }

    public class WifiBean {
        private RadioBean radio;
        private List<APBean> ap;

        public RadioBean getRadio() {
            return radio;
        }

        public void setRadio(RadioBean radio) {
            this.radio = radio;
        }

        public List<APBean> getAp() {
            return ap;
        }

        public void setAp(List<APBean> ap) {
            this.ap = ap;
        }

        @Override
        public String toString() {
            return "WifiBean{" +
                    "radio=" + radio +
                    ", ap=" + ap +
                    '}';
        }
    }

    public class RadioBean {
        private String mode;
        private String currentChannel;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getCurrentChannel() {
            return currentChannel;
        }

        public void setCurrentChannel(String currentChannel) {
            this.currentChannel = currentChannel;
        }

        @Override
        public String toString() {
            return "RadioBean{" +
                    "mode='" + mode + '\'' +
                    ", currentChannel='" + currentChannel + '\'' +
                    '}';
        }
    }

    public class APBean {
        private String ssid;
        private String key;
        private String auth;
        private String encrypt;

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getAuth() {
            return auth;
        }

        public void setAuth(String auth) {
            this.auth = auth;
        }

        public String getEncrypt() {
            return encrypt;
        }

        public void setEncrypt(String encrypt) {
            this.encrypt = encrypt;
        }

        @Override
        public String toString() {
            return "APBean{" +
                    "ssid='" + ssid + '\'' +
                    ", key='" + key + '\'' +
                    ", auth='" + auth + '\'' +
                    ", encrypt='" + encrypt + '\'' +
                    '}';
        }
    }
}
