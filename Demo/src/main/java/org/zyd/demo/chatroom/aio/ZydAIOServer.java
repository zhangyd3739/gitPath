package org.zyd.demo.chatroom.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ZydAIOServer {

	/**
	 * 服务端口
	 */
	static final int SERVER_PROT = 16888;

	/**
	 * 客户端列表
	 */
	public static volatile List<AsynchronousSocketChannel> clientList = new ArrayList<AsynchronousSocketChannel>();
	
	private static final Object waitObject = new Object();

	/**
	 * 字符集
	 */
	private final Charset charset = Charset.forName("UTF-8");

	/**
	 * 服务端异步通道
	 */
	private AsynchronousServerSocketChannel serverChannel = null;

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
		new ServerSocketChannelHandler();
		// 注册事件和事件完成后的处理器，这个CompletionHandler就是事件完成后的处理器，为AsynchronousServerSocketChannel注册监听，注意只是为AsynchronousServerSocketChannel通道注册监听，并不包括为 随后客户端和服务器 socketchannel通道注册的监听
		serverChannel.accept(this, );
	}


	public static void main(String[] args) throws InterruptedException {

		new ZydAIOServer().start();

		//等待，以便观察现象（这个和要讲解的原理本身没有任何关系，只是为了保证守护线程不会退出）
        synchronized(waitObject) {
            waitObject.wait();
        }
	}
}
