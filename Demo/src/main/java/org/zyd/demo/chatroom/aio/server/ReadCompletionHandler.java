package org.zyd.demo.chatroom.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadCompletionHandler implements CompletionHandler<Integer, AsynchronousSocketChannel> {

	private ByteBuffer buffer;

	public ReadCompletionHandler(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public void completed(Integer result, AsynchronousSocketChannel attachment) {
		try {
			buffer.flip();
			// System.out.println("读取的内容：" + charset.decode(buffer));
			// 向每个客户端输出
			for (AsynchronousSocketChannel clientChannel : ZydAIOServer.clientList) {
				clientChannel.write(buffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 循环监听读数据事件
			this.buffer = ByteBuffer.allocate(1024);;
			attachment.read(buffer, attachment, this);
		}
	}

	public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
		System.out.println("失败：" + exc);
	}
}
