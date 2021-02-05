package com.flappy.smartdiff.util.tcp;



import android.util.Log;

import com.flappy.smartdiff.util.tcp.listener.RspHandler;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NioClient implements Runnable {
    private static final String TAG = "NioClient";
    private final static int MAX_BUFFER_SIZE = 1 * 1024 * 1024;
    private InetAddress hostAddress;
    private int port;
    // The selector we'll be monitoring
    private Selector selector;
    private int timeOut = 60 * 1000;
    // The buffer into which we'll read data when it's available
    private ByteBuffer readBuffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);
    // A list of PendingChange instances
    private List pendingChanges = new LinkedList();
    private boolean runnning = true;
    // Maps a SocketChannel to a list of ByteBuffer instances
    private Map pendingData = new HashMap();
    private Map channelMap = new HashMap();
    // Maps a SocketChannel to a RspHandler
    private Map rspHandlers = Collections.synchronizedMap(new HashMap());
    private int errorsTag = 0;
    private RspHandler mRspHander;
    private SocketChannel socketChannel;
    private Thread daemonThread;

    public NioClient() {
    }

    private void send(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        //Log.w("#########write.queue.size: " + pendingData.size());
        synchronized (this.pendingData) {
            List queue = (List) this.pendingData.get(socketChannel);
            // Write until there's not more data ...
            while (queue != null && !queue.isEmpty()) {
                PacketBuffer buffer = (PacketBuffer) queue.remove(0);
                if (buffer != null && buffer.getData() != null) {
                    ByteBuffer buf = ByteBuffer.wrap(buffer.getData());
                    errorsTag = RspHandler.WRITE_EXCEPTION;
                    try {
                        int sendNum = 0;
                        while (buf.hasRemaining()) {
                            int len = socketChannel.write(buf);
                            sendNum += len;
                            if (len < 0) {
                                RspHandler handler = (RspHandler) this.rspHandlers.get(socketChannel);
                                if (handler != null) {
                                    handler.onWriteError(errorsTag, key, new EOFException("send error : " + len));
                                }
                                break;
                            }
                            if (len == 0) {
                                key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                                selector.wakeup();
                                break;
                            }
                        }
//                        sendNum = socketChannel.write(buf);


                        if (buffer.getOnTransListener() != null) {
                            buffer.getOnTransListener().onSucessfull();
                            if (buffer.getOnTransListener().getOnDataTransforListener() != null) {
                                buffer.getOnTransListener().getOnDataTransforListener().onSendAfter(sendNum);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (buffer.getOnTransListener() != null) {
                            buffer.getOnTransListener().onFailed(e);
                        }
                        if (socketChannel != null) {
                            RspHandler handler = (RspHandler) this.rspHandlers.get(socketChannel);
                            if (handler != null) {
                                handler.onWriteError(errorsTag, key, e);
                            }
                        }
                    }
                    if (buf.remaining() > 0) {
                        // ... or the socket's buffer fills up
                        break;
                    }
                }
//                queue.remove(0);
            }

            if (queue == null || queue.isEmpty()) {
                // We wrote away all data, so we're no longer interested
                // in writing on this socket. Switch back to waiting for
                // data.
                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }

    public synchronized void open() throws IOException {
        // Start a new connection
        if (isConnected()) {
            Log.i(TAG, "uu#######isConnected " + socketChannel.isConnected() + mRspHander + " " + mRspHander.getOnTransListener());
            if (mRspHander != null && mRspHander.getOnTransListener() != null) {
                mRspHander.getOnTransListener().onSucessfull();
            }
            return;
        }
        close();
//        closeSelect();
        this.selector = this.initSelector();
        socketChannel = this.initiateConnection();
        if (mRspHander != null && socketChannel != null) {
            this.rspHandlers.put(socketChannel, mRspHander);
        }
        daemonThread = new Thread(this);
//        daemonThread.setDaemon(true);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        System.out.println(sf.format(new Date()));
        daemonThread.setName("NioClient-" + sf.format(new Date()));
        runnning = true;
        daemonThread.start();
    }

    public synchronized void open(String ip, int port) throws IOException {
        this.hostAddress = InetAddress.getByName(ip);
        this.port = port;
        open();
    }

    public synchronized void send(PacketBuffer data, RspHandler handler) throws IOException {
        if (socketChannel == null) {
            // Start a new connection
            socketChannel = this.initiateConnection();
        }
        // Register the response handler
        if (handler != null) {
            this.rspHandlers.put(socketChannel, handler);
        }

        // And queue the data we want written
        synchronized (this.pendingData) {
            List queue = (List) this.pendingData.get(socketChannel);
            if (queue == null) {
                queue = new ArrayList();
                this.pendingData.put(socketChannel, queue);
            }
            queue.add(data);//ByteBuffer.wrap(data)
//            Log.i(TAG, "uu#########send: " + "ip=" + hostAddress + ":" + port + " " + ByteUtils.toHexStrings(data.getData()));
        }

        // Finally, wake up our                  selecting thread so it can make the required changes
//        this.selector.wakeup();
//        SelectionKey key = socketChannel.register(selector, SelectionKey.OP_WRITE);
//        this.selector.wakeup();

        SelectionKey key = socketChannel.keyFor(this.selector);
        if (key != null) {
            boolean valid = key.isValid();
//            Log.w("@@@@@@@@@@@@@@@@@@@@@@@@@@@@:" + valid);
            if (valid) {
                key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
            }
        }
        if (selector != null) {
            this.selector.wakeup();
        }
    }

    public void send(PacketBuffer data) throws IOException {
        pendingData.clear();
        this.send(data, this.mRspHander);
    }

    public void setListener(RspHandler resposeHandler) {
        this.mRspHander = resposeHandler;
    }

    public boolean isChannelConnect() {
        boolean connect = false;
        if (socketChannel == null) {
            return connect;
        }
        Object obj = channelMap.get(socketChannel);
        if (obj == null) {
            Log.e(TAG, "uu#########get channelMap is null");
            return false;
        } else {
            Object value = channelMap.get(socketChannel);
            if (value != null) {
                connect = (boolean) value;//socketChannel.isConnected();
            }
//            Log.e(TAG, "uu########" + (connect ? "channel is connected" : "channel is not connect") + " socket.isConnected:" + socketChannel.socket().isConnected());
        }
        return connect;
    }

    private boolean isConnected() {
        return isChannelConnect();//socketChannel != null && socketChannel.isConnected();
    }

    public boolean isOpen() {
        if (socketChannel == null) {
            return false;
        }
        boolean connect = socketChannel.isOpen();
        Log.e(TAG, "uu##########" + connect + "" + socketChannel.isOpen());
        return connect;
    }

    private void shutdown() {
        try {
            if (null != daemonThread && daemonThread.isAlive()) {
                daemonThread.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            daemonThread = null;
        }

        if (socketChannel != null) {
            try {
                socketChannel.close();
                while (socketChannel.isOpen()) {
                    try {
                        Thread.sleep(300);
                    } catch (final InterruptedException e) {
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }
                    socketChannel.close();
                }
//                Log.e("端口关闭成功");
            } catch (final IOException e) {
                Log.e(TAG, "端口关闭错误:" + e);
                e.printStackTrace();
            } finally {
                socketChannel = null;
            }
        }
        // 关闭端口选择器
        closeSelect();
    }

    /**
     * 关闭端口选择器
     */
    private void closeSelect() {
        if (selector != null) {
            try {
                selector.close();
                Log.e(TAG, "端口选择器关闭成功");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                selector = null;
            }
        }
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public void run() {
        while (runnning) {
            SelectionKey localKey = null;
            // Process any pending changes
            try {
                synchronized (this.pendingChanges) {
                    Iterator changes = this.pendingChanges.iterator();
                    while (changes.hasNext()) {
                        ChangeRequest change = (ChangeRequest) changes.next();
                        if (selector == null)
                            return;
                        switch (change.type) {
                            case ChangeRequest.CHANGEOPS:
                                SelectionKey key = change.socket.keyFor(this.selector);
                                localKey = key;
                                key.interestOps(change.ops);
                                break;
                            case ChangeRequest.REGISTER:
                                change.socket.register(this.selector, change.ops);
                                break;
                        }
                    }
                    this.pendingChanges.clear();
                    this.pendingChanges.notifyAll();
                }
                if (selector == null)
                    break;
                // Wait for an event one of the registered channels
//                Log.e("Wait for an event one of the registered channels");
                int selectValue = this.selector.select();
                // Iterate over the set of keys for which events are available
                if (this.selector.selectedKeys() != null) {
                    Iterator selectedKeys = this.selector.selectedKeys().iterator();
                    while (selectedKeys.hasNext()) {
                        SelectionKey key = (SelectionKey) selectedKeys.next();
                        localKey = key;
                        selectedKeys.remove();
                        if (!key.isValid()) {
                            continue;
                        }

                        // Check what event is available and deal with it
                        if (key.isConnectable()) {
                            this.finishConnection(key);
                        } else if (key.isReadable()) {
                            this.read(key);
                        } else if (key.isWritable()) {
                            this.send(key);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                catchException(localKey, e);
                close();
            }
        }
    }

    public synchronized void close() {
        runnning = false;
        shutdown();
        pendingData.clear();
        pendingChanges.clear();
        channelMap.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (pendingChanges) {
                    pendingChanges.notifyAll();
                }
            }
        }, "nio.close").start();
    }

    private void catchException(SelectionKey localKey, Exception e) {
        if (socketChannel != null) {
            channelMap.put(socketChannel, false);
            RspHandler handler = (RspHandler) this.rspHandlers.get(socketChannel);
            if (handler != null) {
                if (e instanceof ClosedChannelException) {
                    handler.onDisconnect(localKey, e);
                    Log.e(TAG, "#######" + hostAddress + ":" + port + "连接断开");
                } else if (e instanceof SocketTimeoutException) {
                    Log.e(TAG, "#######" + hostAddress + ":" + port + "连接超时");
                    handler.onConnectfailed(errorsTag, localKey, e);
                } else if (e instanceof IOException) {
                    handler.onConnectfailed(errorsTag, localKey, e);
                    Log.e(TAG, "#######" + hostAddress + ":" + port + "连接IO异常:" + e.toString());
                } else if (e instanceof ClosedSelectorException) {
                    handler.onConnectfailed(errorsTag, localKey, e);
                    Log.e(TAG, "#######" + hostAddress + ":" + port + "连接异常:" + e.toString());
                } else {
                    handler.onConnectfailed(errorsTag, localKey, e);
                    Log.e(TAG, "#######" + hostAddress + ":" + port + "连接异常:" + e.toString());
                }
            }
        }
//        if (e != null)
//            Log.w(TAG, "#########run " + e.getMessage() == null ? e.toString() : e.getMessage());
//        else
//            Log.w(TAG, "#########run " + errorsTag);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        // Clear out our read buffer so it's ready for new data
        this.readBuffer.clear();
//        System.out.println("###################  "+key.channel().toString());
        // Attempt to read off the channel
        int numRead;
        try {
            numRead = socketChannel.read(this.readBuffer);
//            System.out.println("#######读取长度:"+numRead);
            channelMap.put(socketChannel, true);
        } catch (IOException e) {
            // The remote forcibly closed the connection, cancel
            // the selection key and close the channel.
            Log.e(TAG, "uu#######read已断开连接,IP:" + hostAddress + ":" + port);
            key.cancel();
            channelMap.put(socketChannel, false);
            RspHandler handler = (RspHandler) this.rspHandlers.get(socketChannel);
            if (handler != null) {
                handler.onDisconnect(key, e);
            }
            errorsTag = RspHandler.CLOSE_EXCEPTION;
            socketChannel.close();
            return;
        }

        //如果返回的是-1,那么意味着通道内的数据已经读取完毕，到底了（链接关闭）。
        if (numRead == -1) {
            // Remote entity shut the socket down cleanly. Do the
            // same from our end and cancel the channel.
            errorsTag = RspHandler.CLOSE_EXCEPTION;
            key.channel().close();
            key.cancel();
            Log.e(TAG, "uu~#######numRead=-1 已断开连接,IP:" + hostAddress + ":" + port);
            channelMap.put(socketChannel, false);
            RspHandler handler = (RspHandler) this.rspHandlers.get(socketChannel);
            if (handler != null) {
                handler.onDisconnect(key, new Exception("Connection reset by peer"));
            }
            return;
        }

        // Handle the response
        this.handleResponse(socketChannel, this.readBuffer.array(), numRead);
    }

    private void handleResponse(SocketChannel socketChannel, byte[] data, int numRead) throws IOException {
//        Log.e(numRead+" ###uulog TCP接受数据:" + ByteUtils.toHexString(data));
//        byte[] rspData = checkData(data, numRead, "");
//        if (rspData == null)
//            return;
        //Log.e("###tcp.dispatch.complete:" + ByteUtils.toHexString(rspData));
        // Make a correctly sized copy of the data before handing it
        // to the client
//        Log.d(numRead+":"+ByteUtils.toHexString(data),true);
        byte[] rspData = new byte[numRead];
        System.arraycopy(data, 0, rspData, 0, numRead);

        // Look up the handler for this channel
        RspHandler handler = (RspHandler) this.rspHandlers.get(socketChannel);
        Log.e(TAG, "#########uulog TCP接受数据:" + ByteUtils.toHexStringHexo(rspData));
        // And pass the response to it
        if (handler != null && handler.onRecevie(rspData)) {
            // The handler has seen enough, close the connection
            Log.e(TAG, "uu#######handleResponse已断开连接,IP:" + hostAddress + ":" + port);
//            channelMap.put(socketChannel,false);
//            socketChannel.close();
//            socketChannel.keyFor(this.selector).cancel();
        }
    }


    private void finishConnection(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        // Finish the connection. If the connection operation failed
        // this will raise an IOException.
        try {
            boolean finish = socketChannel.finishConnect();
            Log.e(TAG, "uu#######finishConnection已连接,IP:" + hostAddress + ":" + port + " finishconnect:" + finish);
            if (finish) {
                channelMap.put(socketChannel, finish);
                key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
            }
            RspHandler handler = (RspHandler) this.rspHandlers.get(socketChannel);
            if (handler != null) {
                handler.onConnect(key);
                if (handler.getOnTransListener() != null) {
                    handler.getOnTransListener().onSucessfull();
                }
            }

        } catch (IOException e) {
            // Cancel the channel's registration with our selector
            Log.e(TAG, "uu#########finishConnection.exception:" + e.toString());
            Log.e(TAG, "uu#######finishConnection已断开连接,IP:" + hostAddress + ":" + port);
            key.cancel();
            channelMap.put(socketChannel, false);
            RspHandler handler = (RspHandler) this.rspHandlers.get(socketChannel);
            if (handler != null) {
                handler.onConnectfailed(RspHandler.CHANNEL_CLOSED, key, e);
                if (handler.getOnTransListener() != null) {
                    handler.getOnTransListener().onFailed(e);
                }
            }
            return;
        }

        // Register an interest in writing on this channel
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private SocketChannel initiateConnection() throws IOException {
        // Create a non-blocking socket channel
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        // Kick off connection establishment
        socketChannel.connect(new InetSocketAddress(this.hostAddress, this.port));

        // Queue a channel registration since the caller is not the
        // selecting thread. As part of the registration we'll register
        // an interest in connection events. These are raised when a channel
        // is ready to complete connection establishment.
        synchronized (this.pendingChanges) {
            if (hostAddress != null) {
                Log.w(TAG, "connecting to " + hostAddress.getHostAddress() + ":" + port);
            }
            this.pendingChanges.add(new ChangeRequest(socketChannel, ChangeRequest.REGISTER, SelectionKey.OP_CONNECT));
            this.pendingChanges.notifyAll();
        }

        return socketChannel;
    }

    private Selector initSelector() throws IOException {
        // Create a new selector
        return SelectorProvider.provider().openSelector();
    }

}
