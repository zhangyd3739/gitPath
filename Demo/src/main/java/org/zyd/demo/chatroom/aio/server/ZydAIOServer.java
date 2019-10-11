package org.zyd.demo.chatroom.aio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
		// 注册接收事件和事件完成后的处理器
		serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

			public void completed(AsynchronousSocketChannel result, Object attachment) {
				System.out.println("线程名称：" + Thread.currentThread().getName());
				try {
					// 把每个客户端通道保存到客户端列表
					clientList.add(result);

					final ByteBuffer buffer = ByteBuffer.allocate(1024);

					// result.read(buffer).get(100, TimeUnit.SECONDS);

					result.read(buffer, result, new CompletionHandler<Integer, AsynchronousSocketChannel>() {

						public void completed(Integer result, AsynchronousSocketChannel attachment) {
							buffer.flip();
							// 向每个客户端输出
							for (AsynchronousSocketChannel clientChannel : clientList) {
								clientChannel.write(buffer);
							}
							buffer.clear();
							
							// 循环监听读数据事件
							attachment.read(buffer, attachment, this);

							buffer.clear();
						}

						public void failed(Throwable exc, AsynchronousSocketChannel attachment) {

						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 循环监听接收请求事件
					serverChannel.accept(null, this);
				}
				System.out.println("end");
			}

			public void failed(Throwable exc, Object attachment) {
				System.out.println("失败：" + exc);
			}
		});
	}

	public static void main(String[] args) throws InterruptedException {

		new ZydAIOServer().start();

		// 等待，以便观察现象（这个和要讲解的原理本身没有任何关系，只是为了保证守护线程不会退出）
		synchronized (waitObject) {
			waitObject.wait();
		}
	}
}
