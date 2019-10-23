package org.zyd.demo.close.io;

import java.io.FileInputStream;

/**
 * 测试IO close
 * 
 * cd /home/dev/classes
 * 
 * java -XX:+PrintGCDetails org.zyd.demo.close.io.TestCloseIO1
 * 
 * linux查看程序占用文件命令: /usr/sbin/lsof -p 6692 -r5
 * 
 * @author zhangyd
 *
 */
public class TestCloseIO1 {
	
	static final String filePath1 = "/logs/test1.log";
	
	static final String filePath2 = "/logs/test2.log";

	public static void main(String[] args) throws Exception {
		FileInputStream fis1 = new FileInputStream(filePath1);
		System.out.println("打开文件:" + filePath1);
		if (true) {
			FileInputStream fis2 = new FileInputStream(filePath2);
			System.out.println("打开文件:" + filePath2);
		}
		Thread.sleep(60 * 1000);
		System.gc();
		Thread.sleep(10 * 60 * 1000);
	}

}
