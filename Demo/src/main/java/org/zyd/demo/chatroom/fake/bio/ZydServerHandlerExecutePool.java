package org.zyd.demo.chatroom.fake.bio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 服务端线程池
 * @author zhangyd
 *
 */
public class ZydServerHandlerExecutePool {
	
	private ExecutorService executor;
	
	public ZydServerHandlerExecutePool(int maxPoolSize, int queueSize){
		executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
	}
	
	public void execute(Runnable task) {
		executor.execute(task);
	}
}
