package org.zyd.demo.chatroom.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

	public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
		System.out.println("线程名称：" + Thread.currentThread().getName());
		try {
			// 把每个客户端通道保存到客户端列表
			ZydAIOServer.clientList.add(result);

			ByteBuffer buffer = ByteBuffer.allocate(1024);

			result.read(buffer, result, new ReadCompletionHandler(buffer));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 循环监听接收请求事件
			attachment.accept(attachment, this);
		}
		System.out.println("end");
	}

	public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
		System.out.println("失败：" + exc);
	}

}
