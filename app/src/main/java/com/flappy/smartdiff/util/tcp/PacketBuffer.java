package com.flappy.smartdiff.util.tcp;



import com.flappy.smartdiff.util.tcp.listener.OnTransListener;

import java.io.Serializable;

/**
 * Created by uuxia-mac on 15/8/29.
 */
public class PacketBuffer implements Serializable {
    private int id;
    private int command;
    private byte[] data;
    private int length;
    private String ip;
    private int port;
    private long timestamp;
    private int subCmd;
    private OnTransListener onTransListener;


    public PacketBuffer(byte[] data, String ip, int port, int subCmd) {
        this.data = data;
        this.ip = ip;
        this.port = port;
        this.subCmd = subCmd;
    }

    public PacketBuffer(byte[] data, String ip, int port) {
        this.data = data;
        this.ip = ip;
        this.port = port;
    }


    public PacketBuffer() {
        timestamp = System.currentTimeMillis();
    }

    public PacketBuffer(byte[] data) {
        this();
        this.data = data;
    }

    public PacketBuffer(byte[] data, OnTransListener onTransListener) {
        this.data = data;
        this.onTransListener = onTransListener;
    }

    public OnTransListener getOnTransListener() {
        return onTransListener;
    }

    public void setOnTransListener(OnTransListener onTransListener) {
        this.onTransListener = onTransListener;
    }

    public int getSubCmd() {
        return subCmd;
    }

    public void setSubCmd(int subCmd) {
        this.subCmd = subCmd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "PacketBuffer{" +
                "id=" + id +
                ", command=" + command +
                ", data=" + ByteUtils.toHexStringHexo(data) +
                ", length=" + length +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", timestamp=" + timestamp +
                ", subCmd=" + subCmd +
                ", onTransListener=" + onTransListener +
                '}';
    }

    public String toChina() {
        return new String(data);
    }
}
