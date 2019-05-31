package com.luomantic.serialtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.luomantic.serialtest.serial.SerialPortUtil;

import java.util.Arrays;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

public class MainActivity extends AppCompatActivity{
    // 串口可用的波特率
    private String[] botes = new String[]{"0", "50", "75", "110", "134", "150", "200", "300", "600", "1200", "1800", "2400", "4800", "9600", "19200", "38400", "57600", "115200", "230400", "460800", "500000", "576000", "921600", "1000000", "1152000", "1500000", "2000000", "2500000", "3000000", "3500000", "4000000"};

    // 串口号
    private String[] ports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findSerialPort();
        initSerialPort();
    }

    private void initSerialPort() {
        LogUtils.e("打开串口" + SerialPortUtil.open());
        SerialPortUtil.sendString("0x11");
        SerialPortUtil.receive();
    }

    private void findSerialPort() {
        SerialPortFinder finder = new SerialPortFinder();

        // [ttyGS3 (g_serial), ttyGS2 (g_serial), ttyGS1 (g_serial), ttyGS0 (g_serial), ttyS5 (sw_serial), ttyS4 (sw_serial), ttyS3 (sw_serial), ttyS2 (sw_serial), ttyS0 (sw_serial)]
        LogUtils.e(Arrays.asList(finder.getAllDevices()));

        // [/dev/ttyGS3, /dev/ttyGS2, /dev/ttyGS1, /dev/ttyGS0, /dev/ttyS5, /dev/ttyS4, /dev/ttyS3, /dev/ttyS2, /dev/ttyS0]
        LogUtils.e(Arrays.asList(finder.getAllDevicesPath()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("关闭串口" + SerialPortUtil.close());
    }
}
