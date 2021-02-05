package com.flappy.smartdiff.util.tcp.listener;


import com.flappy.smartdiff.util.tcp.PacketBuffer;

public interface IRecevie {
    /**
     * 接受socket数据
     *
     * @param packet 数据包
     */
    void onRecevie(PacketBuffer packet);
}
