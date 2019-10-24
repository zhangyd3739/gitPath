package org.zyd.demo.chatroom.aio.client;

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
		try {
			buffer.flip();
			System.out.println("聊天记录：" + charset.decode(buffer));
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
