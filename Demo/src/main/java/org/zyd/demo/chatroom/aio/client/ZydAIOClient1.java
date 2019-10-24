package org.zyd.demo.chatroom.aio.client;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.TimeUnit;

public class ZydAIOClient {

	/**
	 * 服务端口
	 */
	static final int SERVER_PROT = 16888;
	

	private static final Object waitObject = new Object();

	public static void main(String[] args) throws Exception {

		AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
		client.connect(new InetSocketAddress("localhost", SERVER_PROT));

		client.write(ByteBuffer.wrap("测试：just a test".getBytes())).get();

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.clear();
		client.read(buffer).get(100, TimeUnit.SECONDS);
		buffer.flip();
		System.out.println("received message: " + new String(buffer.array()));

		// 等待，以便观察现象（这个和要讲解的原理本身没有任何关系，只是为了保证守护线程不会退出）
		synchronized (waitObject) {
			waitObject.wait();
		}
	}
}
