package org.zyd.demo.close.io;

import java.io.FileInputStream;
import java.util.concurrent.CountDownLatch;

/**
 * 测试IO close
 * 
 * java -XX:+PrintGCDetails org.zyd.demo.close.io.TestCloseIO2
 * 
 * linux查看程序占用文件命令: /usr/sbin/lsof -p 6692 -r5
 * 
 * @author zhangyd
 *
 */
public class TestCloseIO2 {

	static final int threadSize = 5;

	static final String filePath1 = "/logs/test1.log";

	static final String filePath2 = "/logs/test2.log";

	public static void main(String[] args) throws Exception {
		FileInputStream fis = new FileInputStream(filePath1);
		if (true) {
			FileInputStream fis2 = new FileInputStream(filePath2);
		}
		CountDownLatch end = new CountDownLatch(threadSize);
		for (int i = 0; i < threadSize; i++) {
			String threadName = "thread_" + i;
			MyThead thread = new MyThead(threadName, end);
			thread.start();
		}
		end.await();
		Thread.sleep(60 * 1000);
		System.gc();
		Thread.sleep(10 * 60 * 1000);
	}

}

class MyThead extends Thread {

	private String threadName;

	private CountDownLatch end;

	public MyThead(String threadName, CountDownLatch countDownLatch) {
		this.threadName = threadName;
		this.end = countDownLatch;
	}

	public void run() {
		System.out.println(threadName + " started!");
		try {
			FileInputStream fis = new FileInputStream(TestCloseIO2.filePath2);
			Thread.sleep(60 * 1000);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			end.countDown();
		}
		System.out.println(threadName + " stopped!");
	}
}
