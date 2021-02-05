package com.flappy.smartdiff.util.tcp.listener;

import java.io.Serializable;

/*
 * -----------------------------------------------------------------
 * Copyright ?2014 clife
 * Shenzhen H&T Intelligent Control Co.,Ltd.
 * -----------------------------------------------------------------
 * File: onSessionListener.java
 * Create: 2016/3/2 16:54
 * Author: uuxia
 */
public interface OnSessionListener extends Serializable {
    void connected();

    void disconnect(String error);
}
