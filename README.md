# zip-file-cracker
a password cracker for lot of types of archives, e.g. rar/zip/7z/tar

### Overview
* 这是一个破解加密压缩文件密码的小玩具，使用WinRAR作为解压工具，可以解压RAR（ RAR 5.0需要WinRAR 5.0 ）、ZIP、7Z、ACE、 ARJ、BZ2、CAB、GZ、ISO、JAR、LZ、LZH、TAR、UUE、XZ、Z文件。<br>
  目前rar格式压缩文件在没有针对性破解算法的情况下，只能选择**暴力法**穷举所有可能的密码组合，尽管可以利用多线程提高效率，但它破解密码的速度依然慢（特别是密码位数多于5，且可能出现的字符范围较大时）。所以，看情况使用它。<br>
  
* 需要注意的是，**不能将它用于违法行为**，如果使用者因为使用它而造成违法行为或意外损失，本人并不承担任何责任。

### Instruction
1. 安装[WinRAR个人免费版](http://www.winrar.com.cn/)
2. 修改 /src/main/resources/configuration.xml 文件的配置，包括
```
   WinRAR运行文件路径
   待解密压缩文件路径
   猜测密码的最小长度
   猜测密码的最大长度
   密码可能出现字符的集合
   并发线程数
   其他设置
```  
3. 运行 /src/main/java/main/Main.java 文件 
4. 等待结果，运行时间视密码复杂度而定，如果能确定密码位数，或是密码出现字符，将大大减少运行时间
5. 破解失败，试试调整密码长度、密码字符集

### Examples
测试文件位于/testfiles目录，test.zip和test.rar两种格式压缩文件密码均为22，test2密码为1C2

