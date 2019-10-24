package org.zyd.demo.chatroom.aio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class ZydAIOClient2 {

	// 服务端地址
	static final String ADDRESS = "127.0.0.1";

	// 服务端口
	static final int SERVER_PROT = 16888;

	/**
	 * 字符集
	 */
	private final Charset charset = Charset.forName("UTF-8");

	/**
	 * 客户端异步socket通道
	 */
	AsynchronousSocketChannel clientSocketChannel;

	/**
	 * 构造方法
	 */
	public ZydAIOClient2() {
		this.initClient();
	}

	/**
	 * 初始化方法
	 */
	private void initClient() {
		try {
			clientSocketChannel = AsynchronousSocketChannel.open();
			clientSocketChannel.connect(new InetSocketAddress(ADDRESS, SERVER_PROT));
			System.out.println("AIO客户端2启动");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输入聊天内容
	 */
	public void inputDate() throws IOException {
		Scanner sc = new Scanner(System.in);
		while (true) {
			// 获取控制台输入的消息
			String content = sc.next();
			clientSocketChannel.write(charset.encode(content));
		}
	}

	public void getDate() {
		final ByteBuffer buffer = ByteBuffer.allocate(1024);
		clientSocketChannel.read(buffer, clientSocketChannel, new ReadCompletionHandler(buffer));
	}

	public static void main(String[] args) throws Exception {
		ZydAIOClient2 zydAIOClient = new ZydAIOClient2();
		// 注册读数据事件
		zydAIOClient.getDate();

		// 从控制台输入消息
		zydAIOClient.inputDate();
	}
}
