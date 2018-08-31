/**
 * 
 */
package com.xx.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xx.core.dto.LinkCheckMessage;
import com.xx.core.dto.Message;
import com.xx.util.Crc8Util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author lee
 *
 */
public class LinkCheckSendThread implements Runnable {
	
	private Logger log = LoggerFactory.getLogger(LinkCheckSendThread.class);
	
	private LinkCheckMessage message;
	
	private Channel channel;
	/**
	 * 间隔时间 单位-秒
	 */
	private int interval;
	
	
	

	public LinkCheckSendThread(final LinkCheckMessage message,final Channel channel,final int interval) {
		super();
		this.message = message;
		this.channel = channel;
		this.interval = interval;
	}




	@Override
	public void run() {
		while(true) {
			try {
				for (Message msg : message.toMessage()) {
					
					this.channel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
						
						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							if(future.isSuccess()) {
								log.info(String.format("发送数据：%s", Crc8Util.formatHexString(msg.toHexString())));
							}
						}
					});
				}
				Thread.sleep(this.interval * 1000L);
			} catch (InterruptedException e) {
				log.error("链路检测发送线程中断异常：",e);
				break;
			}
		}
	}

}
