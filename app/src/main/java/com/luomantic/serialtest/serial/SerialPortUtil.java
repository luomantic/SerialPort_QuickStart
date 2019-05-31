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
    private static boolean isFlagSerial = false;

    private static InputStream inputStream = null;
    private static OutputStream outputStream = null;
    private static Thread receiveThread = null;
    private static String strData = "";

    /**
     * 打开串口
     */
    public static boolean open() {
        boolean isOpen;
        if(isFlagSerial){
            LogUtils.e("串口已经打开");
            return false;
        }
        try {
            SerialPort serialPort = new SerialPort(new File("/dev/ttyS4"), 115200, 0);
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            receive();
            isOpen = true;
            isFlagSerial = true;
        } catch (IOException e) {
            e.printStackTrace();
            isOpen = false;
        }
        return isOpen;
    }

    /**
     * 关闭串口
     */
    public static boolean close() {
        if(isFlagSerial){
            LogUtils.e("串口关闭失败");
            return false;
        }
        boolean isClose;
        LogUtils.e( "关闭串口");
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            isClose = true;
            isFlagSerial = false;//关闭串口时，连接状态标记为false
        } catch (IOException e) {
            e.printStackTrace();
            isClose = false;
        }
        return isClose;
    }

    /**
     * 发送串口指令
     */
    public static void sendString(String data) {
        if (!isFlagSerial) {
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
        if (receiveThread != null && !isFlagSerial) {
            return;
        }
        receiveThread = new Thread() {
            @Override
            public void run() {
                while (isFlagSerial) {
                    try {
                        byte[] readData = new byte[32];
                        if (inputStream == null) {
                            return;
                        }
                        int size = inputStream.read(readData);
                        if (size > 0 && isFlagSerial) {
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
