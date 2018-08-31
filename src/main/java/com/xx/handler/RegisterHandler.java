package com.xx.handler;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xx.core.dto.LinkCheckMessage;
import com.xx.core.dto.Message;
import com.xx.core.dto.RegisterMessage;
import com.xx.util.Crc8Util;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class RegisterHandler extends ChannelInboundHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(RegisterHandler.class);

	private String clientId;
	/**
	 * 注册消息发送次数
	 */
	private static final AtomicInteger REG_COUNT = new AtomicInteger(0);

	/**
	 * 注册消息
	 */
	private RegisterMessage register;

	public RegisterHandler(String clientId, RegisterMessage register) {
		super();
		this.clientId = clientId;
		this.register = register;
	};

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Message in = (Message) msg;
		try {
			if(LinkCheckMessage.isLinkCheck(in)) {
				if (LinkCheckMessage.isOnline(in) && REG_COUNT.incrementAndGet() <= 1) {
					Thread registerThread = new Thread(new Runnable() {

						@Override
						public void run() {
							// 收到连接检测保持在线返回,且为首次收到
							for (Message m : register.toMessage()) {
								log.info(String.format("客户端[%s]发送注册数据：%s", clientId, Crc8Util.formatHexString(m.toHexString())));
								ctx.channel().writeAndFlush(m);
							}
						}
					});
					registerThread.setDaemon(true);
					registerThread.start();
					
				}
			}else {
				log.info(String.format("客户端[%s]收到注册数据：%s", clientId, Crc8Util.formatHexString(in.toHexString())));
				if (!RegisterMessage.isRegister(in)) {
					// 不是注册信息，交给下一个处理处理
					ctx.fireChannelRead(in);
					return;
				}
				
			}
		} finally {
			// ByteBuf是一个引用计数对象，这个对象必须显示地调用release()方法来释放
			// or ((ByteBuf)msg).release();
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (null != cause) {
			log.error(String.format("客户端[%s]异常！", clientId), cause);
		}
		if (null != ctx) {
			ctx.close();
		}
	}

}
