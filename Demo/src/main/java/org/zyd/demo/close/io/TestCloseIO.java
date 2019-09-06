package org.zyd.demo.close.io;

import java.io.FileInputStream;
import java.util.concurrent.CountDownLatch;

/**
 * 测试IO close
 * 
 * linux查看文件占用: /usr/sbin/lsof -p 6692 -r5
 * @author zhangyd
 *
 */
public class TestCloseIO {

	static final int threadSize = 10;

	public static void main(String[] args) throws Exception {
		FileInputStream fis = new FileInputStream("/logs/test1.log");
		CountDownLatch end = new CountDownLatch(threadSize);
		for (int i = 0; i < threadSize; i++) {
			String threadName = "thread_" + i;
			MyThead thread = new MyThead(threadName, end);
			thread.start();
		}
		end.await();
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
			FileInputStream fis = new FileInputStream("/logs/test2.log");
			Thread.sleep(60 * 1000);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			end.countDown();
		}
		System.out.println(threadName + " stopped!");
	}
}
