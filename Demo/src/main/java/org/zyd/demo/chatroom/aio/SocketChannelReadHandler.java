package org.zyd.demo.chatroom.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class SocketChannelReadHandler implements CompletionHandler<Integer, StringBuffer> {

	private AsynchronousSocketChannel socketChannel;

	private ByteBuffer byteBuffer;

	public SocketChannelReadHandler(AsynchronousSocketChannel socketChannel, ByteBuffer byteBuffer) {
		super();
		this.socketChannel = socketChannel;
		this.byteBuffer = byteBuffer;
	}

	public void completed(Integer result, StringBuffer attachment) {
		// 如果条件成立，说明客户端主动终止了TCP套接字，这时服务端终止就可以了
		if (result == -1) {
			try {
				this.socketChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		/**
		 * 由于我们从Integer result知道了本次channel从操作系统获取数据总长度
		 * 所以实际上，我们不需要切换成“读模式”的，但是为了保证编码的规范性，还是建议进行切换。
		 **/
		this.byteBuffer.flip();
		byte[] contexts = new byte[1024];
		this.byteBuffer.get(contexts, 0, result);
		this.byteBuffer.clear();
		try {
			String nowContent = new String(contexts, 0, result, "UTF-8");
			attachment.append(nowContent);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 如果条件成立，说明还没有接收到“结束标记”
		if (attachment.indexOf("over") == -1) {
			return;
		} else {
			// 清空已经读取的缓存，并从新切换为写状态(这里要注意clear()和capacity()两个方法的区别)
			this.byteBuffer.clear();

			// ======================================================
			// 当然接受完成后，可以在这里正式处理业务了
			// ======================================================

			// 回发数据，并关闭channel
			ByteBuffer sendBuffer = null;
			try {
				sendBuffer = ByteBuffer.wrap(URLEncoder.encode("你好客户端,这是服务器的返回数据", "UTF-8").getBytes());
				socketChannel.write(sendBuffer);
				socketChannel.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// =========================================================================
		// 和上篇文章的代码相同，我们以“over”符号作为客户端完整信息的标记
		// =========================================================================
		attachment = new StringBuffer();

		// 还要继续监听（一次监听一次通知）
		this.socketChannel.read(this.byteBuffer, attachment, this);
	}

	public void failed(Throwable exc, StringBuffer attachment) {
		System.out.println("失败:" + exc);
	}

}
