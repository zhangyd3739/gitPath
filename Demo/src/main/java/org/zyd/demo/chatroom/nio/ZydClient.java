package org.zyd.demo.chatroom.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class ZydClient {

	// 服务端地址
	static final String ADDRESS = "127.0.0.1";

	// 服务端口
	static final int SERVER_PROT = 16888;

	/**
	 * 选择器
	 */
	private Selector selector;

	/**
	 * 客户端socket通道
	 */
	private SocketChannel socketChannel;

	/**
	 * 字符集
	 */
	private final Charset charset = Charset.forName("UTF-8");

	/**
	 * 构造方法
	 */
	public ZydClient() {
		this.initClient();
	}

	/**
	 * 初始化方法
	 */
	private void initClient() {
		try {
			selector = Selector.open();
			socketChannel = SocketChannel.open(new InetSocketAddress(ADDRESS, SERVER_PROT));
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("NIO客户端启动");
	}

	/**
	 * 输入聊天内容
	 */
	public void inputDate() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			// 获取控制台输入的消息
			String content = sc.next();
			try {
				socketChannel.write(charset.encode(content));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void getDate() {
		new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						// 阻塞 直到有就绪事件为止
						selector.select();
						Set<SelectionKey> selectedKeys = selector.selectedKeys();
						Iterator<SelectionKey> iterator = selectedKeys.iterator();
						SelectionKey selectionKey = null;
						while (iterator.hasNext()) {
							selectionKey = iterator.next();
							// 判断是否有效
							if (selectionKey.isValid()) {
								if (selectionKey.isReadable()) {
									SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
									// 读取服务端消息
									readMessage(socketChannel);
								}
							}
							// 必须removed 否则会继续存在，下一次循环还会进来,
							iterator.remove();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 读取服务端消息
	 * 
	 * @param socketChannel
	 * @throws IOException
	 */
	private void readMessage(SocketChannel socketChannel) throws IOException {
		StringBuilder message = new StringBuilder();
		// 从客户端通道获取输入数据
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		while (socketChannel.read(byteBuffer) > 0) {
			// 读写转换
			byteBuffer.flip();
			message.append(charset.decode(byteBuffer));
			byteBuffer.clear();
		}
		System.out.println("聊天记录：" + message.toString());
	}

	public static void main(String[] args) throws Exception {
		ZydClient zydClient = new ZydClient();
		// 启动线程获取消息
		zydClient.getDate();
		// 从控制台输入消息
		zydClient.inputDate();
	}

}
