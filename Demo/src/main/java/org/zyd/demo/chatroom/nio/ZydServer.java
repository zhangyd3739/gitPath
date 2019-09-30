package org.zyd.demo.chatroom.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ZydServer {

	/**
	 * 服务端口
	 */
	static final int SERVER_PROT = 16888;

	/**
	 * 选择器
	 */
	private Selector selector;

	/**
	 * 服务端socket通道
	 */
	private ServerSocketChannel serverChannel;
	
	/**
	 * 客户端列表
	 */
	public static volatile List<SocketChannel> clientList = new ArrayList<SocketChannel>();

	/**
	 * 构造方法
	 * @param port
	 */
	public ZydServer() {
		this.initServer();
	}
	
	/**
	 * 字符集
	 */
	private final Charset charset = Charset.forName("UTF-8");

	/**
	 * 初始化
	 * 
	 * @param port
	 */
	private void initServer() {
		try {
			// 打开一个通道
			serverChannel = ServerSocketChannel.open();
			// 设置通道非阻塞
			serverChannel.configureBlocking(false);
			// 绑定端口号
			serverChannel.socket().bind(new InetSocketAddress(SERVER_PROT));
			// 打开选择器
			selector = Selector.open();
			/**
			 * 通道注册到选择器， 设置监听事件（多个事件：SelectionKey.OP_READ | SelectionKey.OP_WRITE）
			 * SelectionKey.OP_CONNECT
			 * SelectionKey.OP_ACCEPT
			 * SelectionKey.OP_READ
			 * SelectionKey.OP_WRITE
			 */
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println("NIO,聊天室服务端启动，端口：" + SERVER_PROT);
	}

	/**
	 * 服务端监听客户端消息
	 */
	public void listener() {
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
						// 判断是否客户端请求连接
						if (selectionKey.isAcceptable()) {
							ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
							SocketChannel sc = ssc.accept();
							sc.configureBlocking(false);
							sc.register(selector, SelectionKey.OP_READ);
							// 将客户端通道放入集合
							clientList.add(sc);
						} else if (selectionKey.isReadable()) {
							SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
							// 发送消息
							this.sendMessage(socketChannel);
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

	/**
	 * 发送消息
	 * 
	 * @param socketChannel
	 * @throws IOException
	 */
	private void sendMessage(SocketChannel socketChannel) throws IOException {
		StringBuilder message = new StringBuilder();
		// 从客户端通道获取输入数据
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		while (socketChannel.read(byteBuffer) > 0) {
			// 读写转换
			byteBuffer.flip();
			message.append(charset.decode(byteBuffer));
			byteBuffer.clear();
		}
		// 读出来的消息重写回各客户端
		if(message.length() > 0) {
			for (SocketChannel clientChannel : clientList) {
				clientChannel.write(charset.encode(message.toString()));
			}
		}
	}

	
	public static void main(String[] args) {
		ZydServer zydServer = new ZydServer();
		zydServer.listener();
	}
}
