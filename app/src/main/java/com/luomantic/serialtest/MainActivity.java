package com.luomantic.serialtest;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.luomantic.serialtest.serial.SerialPortUtil;

import java.util.Arrays;

import android_serialport_api.SerialPortFinder;

import static android.graphics.Bitmap.CompressFormat.PNG;

// TODO: 自定义序列化协议, 可以减少传输的数据，提高串口传输速度.
public class MainActivity extends AppCompatActivity {
    // 串口可用的波特率
    private String[] botes = new String[]{"0", "50", "75", "110", "134", "150", "200", "300", "600", "1200", "1800", "2400", "4800", "9600", "19200", "38400", "57600", "115200", "230400", "460800", "500000", "576000", "921600", "1000000", "1152000", "1500000", "2000000", "2500000", "3000000", "3500000", "4000000"};

    // 串口号
    private String[] ports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        testSerialPort();
        test();
    }

    // bitmap转字节数组，并对字节数组进行base64加解密
    private void test() {
        ImageView imageView = findViewById(R.id.image_test);
        Bitmap bitmap = ImageUtils.addCircleBorder(ImageUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.ic_launcher_background)), 50, Color.RED);
        imageView.setImageDrawable(ImageUtils.bitmap2Drawable(bitmap));

        byte[] bytes = ImageUtils.bitmap2Bytes(bitmap, PNG);
        LogUtils.e(ConvertUtils.bytes2HexString(bytes));

        // base64加密
        byte[] encodeBytes = EncodeUtils.base64Encode(bytes);
        LogUtils.e(ConvertUtils.bytes2HexString(encodeBytes));
        LogUtils.e(ConvertUtils.bytes2HexString(EncodeUtils.base64Decode(encodeBytes)));

        Bitmap bitmap1 = ImageUtils.bytes2Bitmap(bytes);
        imageView.setImageBitmap(bitmap1);
    }

    private void testSerialPort() {
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
