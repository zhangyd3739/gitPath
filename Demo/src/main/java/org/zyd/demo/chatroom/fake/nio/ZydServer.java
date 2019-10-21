package org.zyd.demo.chatroom.fake.nio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天室服务端
 */
public class ZydServer {

	// 服务端口
	static final int SERVER_PROT = 16888;

	// 客户端列表
	public static volatile List<Socket> clientList = new ArrayList<Socket>();

	public static void main(String[] args) throws IOException {
		// 创建线程池
		ZydServerHandlerExecutePool singleExecutor = new ZydServerHandlerExecutePool(5, 100);

		// 创建一个ServerSocket，用于监听客户端Socket的连接请求
		ServerSocket server = new ServerSocket(SERVER_PROT);
		System.out.println("伪异步,聊天室服务端启动，端口：" + SERVER_PROT);
		while (true) {
			// 接收客户端连bbbbbbmb接
			Socket socket = server.accept();
			clientList.add(socket);
			// 使用线程池处理消息
			singleExecutor.execute(new ZydServerHandler(socket));
		}
	}

}
