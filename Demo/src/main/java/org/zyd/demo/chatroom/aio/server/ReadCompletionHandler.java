package org.zyd.demo.chatroom.aio.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

public class ReadCompletionHandler implements CompletionHandler<Integer, AsynchronousSocketChannel> {

	private ByteBuffer buffer;
	
	/**
	 * 字符集
	 */
	private final Charset charset = Charset.forName("UTF-8");

	public ReadCompletionHandler(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	public void completed(Integer result, AsynchronousSocketChannel attachment) {
		buffer.flip();
		// System.out.println("读取的内容：" + charset.decode(buffer));
		// 向每个客户端输出
		for (AsynchronousSocketChannel clientChannel : ZydAIOServer.clientList) {
			clientChannel.write(buffer);
		}
		buffer.clear();

		// 循环监听读数据事件
		attachment.read(buffer, attachment, this);
		
		buffer.clear();
	}

	public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
		System.out.println("失败：" + exc);
	}
}
