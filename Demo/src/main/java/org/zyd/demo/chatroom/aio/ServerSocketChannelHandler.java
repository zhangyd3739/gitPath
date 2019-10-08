package org.zyd.demo.chatroom.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ServerSocketChannelHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>{
	

	public void completed(AsynchronousSocketChannel result, AsynchronousServerSocketChannel attachment) {
		/**
		 * 服务端已经接收客户端成功了，为什么还要调用accept方法？因为一个channel可以接收成千上万个客户端
		 * 当调用asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler())方法后，又有新的
		 * 客户端连接接入，所以需要继续调用他的accept方法，接受其它客户端的接入，最终形成一个循环
		 */
		attachment.accept(attachment, this);
		
		ByteBuffer readBuffer = ByteBuffer.allocate(2550);
		result.read(readBuffer, new StringBuffer(), new SocketChannelReadHandler(result , readBuffer));
	}

	public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
		System.out.println("失败:" + exc);
	}
	
}
