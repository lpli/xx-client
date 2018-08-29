/**
 * 
 */
package com.xx.device;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * @author lee
 *
 */
public class SyncFuture<T> implements Future<T> {

	/**
	 * 因为请求和响应是一一对应的，因此初始化CountDownLatch值为1。
	 */
	private CountDownLatch latch = new CountDownLatch(1);
	/**
	 * 需要响应线程设置的响应结果。
	 */
	private T response;
	/**
	 * Futrue的请求时间，用于计算Future是否超时
 	 */
	private long beginTime = System.currentTimeMillis();

	public SyncFuture() {
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		latch.await();
        return this.response;
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		if (latch.await(timeout, unit)) {
            return this.response;
        }
        return null;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDone() {
		if (response != null) {
			return true;
		}
		return false;
	}


	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		// TODO Auto-generated method stub
		return false;
	}
	
	// 用于设置响应结果，并且做countDown操作，通知请求线程
    public void setResponse(T response) {
        this.response = response;
        latch.countDown();
    }
    public long getBeginTime() {
        return beginTime;
    }

}
