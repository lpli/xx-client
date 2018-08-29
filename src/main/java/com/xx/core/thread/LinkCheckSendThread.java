/**
 * 
 */
package com.xx.core.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xx.core.dto.LinkCheckMessage;
import com.xx.core.dto.Message;
import com.xx.util.Crc8Util;

import io.netty.channel.Channel;

/**
 * @author lee
 *
 */
public class LinkCheckSendThread implements Runnable {
	
	private Log log = LogFactory.getLog(LinkCheckSendThread.class);
	
	private LinkCheckMessage message;
	
	private Channel channel;
	/**
	 * 间隔时间 单位-秒
	 */
	private int interval;
	
	
	

	public LinkCheckSendThread(LinkCheckMessage message, Channel channel, int interval) {
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
					log.info(String.format("发送数据：%s", Crc8Util.formatHexString(msg.toHexString())));
					this.channel.writeAndFlush(msg);
				}
				Thread.sleep(this.interval * 1000L);
			} catch (InterruptedException e) {
				log.error("链路检测发送线程中断异常：",e);
				break;
			}
		}
	}

}
