package org.zyd.demo.chatroom.aio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;

public class ZydAIOServer {

	/**
	 * 服务端口
	 */
	static final int SERVER_PROT = 16888;

	/**
	 * 客户端列表
	 */
	public static volatile List<AsynchronousSocketChannel> clientList = new ArrayList<AsynchronousSocketChannel>();

	/**
	 * 服务端异步通道
	 */
	private AsynchronousServerSocketChannel serverChannel = null;

	private static final Object waitObject = new Object();

	public ZydAIOServer() {
		try {
			// 创建异步通道对象
			serverChannel = AsynchronousServerSocketChannel.open();
			// 绑定地址、端口
			serverChannel.bind(new InetSocketAddress(SERVER_PROT));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("AIO,聊天室服务端启动，端口：" + SERVER_PROT);
	}

	public void start() {
		// 注册接收事件和事件完成后的处理器
		serverChannel.accept(serverChannel, new AcceptCompletionHandler());
	}

	public static void main(String[] args) throws InterruptedException {

		new ZydAIOServer().start();

		// 等待，以便观察现象（这个和要讲解的原理本身没有任何关系，只是为了保证守护线程不会退出）
		synchronized (waitObject) {
			waitObject.wait();
		}
	}
}
