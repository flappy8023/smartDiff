package com.flappy.smartdiff.util.tcp;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

public class Utils {
    public static void main(String[] args) throws Exception {
        long num = Math.round(Math.random() * 10000000);
        System.out.println(num);
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static Calendar getCalendar(String strDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(strToDate(strDate));
        return c;
    }

    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    public static String getCurrentTime() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println(sf.format(new Date()));
        return sf.format(new Date());
    }

    public static String getCurTime() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        System.out.println(sf.format(new Date()));
        return sf.format(new Date());
    }

    public static Type getType(Class clazz) {
        Type[] t = clazz.getGenericInterfaces();
        if (t == null)
            return null;
        if (t.length <= 0)
            return null;
        if (t[0] instanceof ParameterizedType) {
            Type[] args = ((ParameterizedType) t[0]).getActualTypeArguments();
            return args[0];
        }
        return null;
    }


    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }


    /**
     * 判断参数是否为数�?
     *
     * @param strNum 待判断的数字参数
     * @return true表示参数为数字，false表示参数非数�?
     */
    public static boolean isNum(final String strNum) {
        return strNum.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    public static String getSSIDForSending(String cap) {
        if (cap != null) {
            if (cap.contains("WEP")) {
                return "WEP";
            } else if (cap.contains("WPA")) {
                //Security.add("WPA-PSK");
                return "WPA-PSK";
            } else {
                return "NONE";

            }
        }
        return "";
    }


    /**
     * json数据前面添加4字节json长度
     *
     * @param json 数据
     * @retrun byte[]
     */
    public static byte[] wrapByteByJson(String json) {

        int lenth = json.length();
        byte[] body = new byte[0];
        try {
            body = json.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ByteBuffer bbuffer = ByteBuffer.allocate(lenth + 4);
        byte[] array = new byte[4];
        array[0] = (byte) (lenth >> 24);
        array[1] = (byte) (lenth >> 16);
        array[2] = (byte) (lenth >> 8);
        array[3] = (byte) lenth;

        bbuffer.put(array);
        bbuffer.put(body);
        bbuffer.flip();

        return bbuffer.array();
    }

    /**
     * json数据前面添加8字节json长度
     *
     * @param json 数据
     * @retrun byte[]
     */
    public static byte[] wrap8ByteByJson(String json) {

        int lenth = json.length();
        byte[] body = new byte[0];
        try {
            body = json.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ByteBuffer bbuffer = ByteBuffer.allocate(lenth + 8);
        byte[] array = new byte[8];
        array[0] = 0x3f;
        array[1] = 0x72;
        array[2] = 0x1f;
        array[3] = (byte) 0xb5;
        array[4] = (byte) (lenth >> 24);
        array[5] = (byte) (lenth >> 16);
        array[6] = (byte) (lenth >> 8);
        array[7] = (byte) lenth;

        bbuffer.put(array);
        bbuffer.put(body);
        bbuffer.flip();

        return bbuffer.array();
    }

    private static final String TAG = "Utils";
    /**
     * json数据前面添加8字节json长度
     *
     * @param body 数据
     * @retrun byte[]
     */
    public static byte[] wrap8ByteBytes(byte[] body) {

        int lenth = body.length;
        ByteBuffer bbuffer = ByteBuffer.allocate(lenth + 8);
        byte[] array = new byte[8];
        array[0] = 0x3f;
        array[1] = 0x72;
        array[2] = 0x1f;
        array[3] = (byte) 0xb5;
        array[4] = (byte) (lenth >> 24);
        array[5] = (byte) (lenth >> 16);
        array[6] = (byte) (lenth >> 8);
        array[7] = (byte) lenth;

        bbuffer.put(array);
        bbuffer.put(body);
        bbuffer.flip();

        return bbuffer.array();
    }

    public static String getBroadcastAddress(Context ctx) {
        try {
            String broad = getBroadcast();
            Log.i(TAG,"get broadcastip：" + broad);
            return broad;
        } catch (SocketException e) {
            e.printStackTrace();
            Log.e(TAG,"get broadcastip failed: " + e.getMessage());
        }
        return null;
    }

    public static String getBroadcast() throws SocketException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements(); ) {
            NetworkInterface ni = niEnum.nextElement();
            if (!ni.isLoopback()) {
                for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                    if (interfaceAddress.getBroadcast() != null && (ni.getName().contains("wlan") || ni.getName().contains("eth1"))) {
                        String broad = interfaceAddress.getBroadcast().toString().substring(1);
                        Log.i("MYACTIVITY", ni.getName() + " 广播地址==" + broad);
                        return broad;
                    }
                }
            }
        }
        return null;
    }


}
