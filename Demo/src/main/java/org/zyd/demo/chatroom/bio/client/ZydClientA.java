package org.zyd.demo.chatroom.bio.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ZydClientA {

	// 服务端地址
	static final String ADDRESS = "127.0.0.1";

	// 服务端口
	static final int SERVER_PROT = 16888;

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket(ADDRESS, SERVER_PROT);
		System.out.println("A启动聊天室");
		
		/**
		 * 因为等待用户输入会导致主线程阻塞
		 * 所以用主线程处理输入，新开一个线程处理读数据
		 */
		new Thread(new ZydClientHandler(socket)).start();

		Scanner sc = new Scanner(System.in);
		while (true) {
			// 获取控制台输入的消息
			String content = sc.next();
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			printWriter.println(content);
			printWriter.flush();
		}
	}
}
