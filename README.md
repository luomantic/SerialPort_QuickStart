# SerialPort_QuickStart
#### 模拟器调试串口
1. USB插入串口线
2. 在android_sdk的tools目录下，执行：  
   ```emulator @emulator_name -scale auto -qemu -serial COM3 &```
   
   说明：  
   ① emulator_name是你的模拟器的名称  
   ② -scale auto表示自动缩放到合适的尺寸  
   ③ -qemu -serial COM8，是将COM8映射到模拟器（COM8是在设备管理中看到的）  
   ④ &是让后台运行  
   执行了该命令之后（模拟器之前并未打开），会弹出配置串口参数的框，


