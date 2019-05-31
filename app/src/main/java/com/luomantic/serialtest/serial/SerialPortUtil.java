package com.luomantic.serialtest.serial;


import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

public class SerialPortUtil {

    /**
     * 标记当前串口状态(true:打开,false:关闭)
     **/
    private static boolean serialOpened = false;

    private static InputStream inputStream = null;
    private static OutputStream outputStream = null;
    private static Thread receiveThread = null;
    private static String strData = "";

    /**
     * 打开串口
     */
    public static boolean open() {
        if(serialOpened){
            LogUtils.e("串口已经打开");
            return false;
        }
        try {
            SerialPort serialPort = new SerialPort(new File("/dev/ttyS4"), 115200, 0);
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            receive();
            serialOpened = true;

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 关闭串口
     */
    public static boolean close() {
        if(serialOpened){
            LogUtils.e("串口关闭失败");
            return false;
        }
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            serialOpened = false;//关闭串口时，连接状态标记为false

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送串口指令
     */
    public static void sendString(String data) {
        if (!serialOpened) {
            LogUtils.e("串口未打开,发送失败" + data);
            return;
        }
        try {
            outputStream.write(ByteUtil.hex2byte(data));
            outputStream.flush();
            LogUtils.e("sendSerialData:" + data);
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("发送指令出现异常");
        }
    }

    /**
     * 接收串口数据的方法
     */
    public static void receive() {
        if (receiveThread != null && !serialOpened) {
            return;
        }
        receiveThread = new Thread() {
            @Override
            public void run() {
                while (serialOpened) {
                    try {
                        byte[] readData = new byte[32];
                        if (inputStream == null) {
                            return;
                        }
                        int size = inputStream.read(readData);
                        if (size > 0 && serialOpened) {
                            strData = ByteUtil.byteToStr(readData, size);
                            LogUtils.e("readSerialData:" + strData);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        receiveThread.start();
    }
}
