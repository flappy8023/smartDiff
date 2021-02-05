package com.flappy.smartdiff.util.tcp.listener;

/*
 * -----------------------------------------------------------------
 * Copyright ?2014 clife
 * Shenzhen H&T Intelligent Control Co.,Ltd.
 * -----------------------------------------------------------------
 * File: OnDataTransforListener.java
 * Create: 2016/3/3 19:01
 * Author: uuxia
 */
public interface OnDataTransforListener {
    void onSendBefore(Object value);

    void onSendAfter(Object value);
}
