package org.zyd.demo.channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class TestFileChannel {

	private static void useFileChannel() {
		RandomAccessFile aFile = null;
		try {
			aFile = new RandomAccessFile("d://新建文本文档.txt", "rw");
			FileChannel inChannel = aFile.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			while (inChannel.read(byteBuffer) != -1) {
				byteBuffer.flip();
				// while (byteBuffer.hasRemaining()) {
				// System.out.print(byteBuffer.get());
				// }
				// 显示乱码，采用默认的编码方式（UTF-16BE）将ByteBuffer转换成CharBuffer
				// CharBuffer charBuffer = byteBuffer.asCharBuffer();
				CharBuffer charBuffer = Charset.forName("GBK").decode(byteBuffer);
				while (charBuffer.hasRemaining()) {
					System.out.print(charBuffer.get());
				}
				byteBuffer.clear();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				aFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		TestFileChannel.useFileChannel();
	}

}
