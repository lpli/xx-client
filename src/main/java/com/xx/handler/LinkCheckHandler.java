package com.xx.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xx.core.dto.LinkCheckMessage;
import com.xx.core.dto.Message;
import com.xx.core.thread.LinkCheckSendThread;
import com.xx.exception.ClientException;
import com.xx.util.Crc8Util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 链路检测处理器
 * 
 * @author lee
 *
 */
public class LinkCheckHandler extends ChannelInboundHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(LinkCheckHandler.class);

	private String clientId;

	private int interval;

	private LinkCheckMessage message;

	private Thread thread;

	public LinkCheckHandler(String clientId, int interval, LinkCheckMessage message) {
		super();
		this.clientId = clientId;
		this.interval = interval;
		this.message = message;
	}

	/**
	 * 通道开启后,发送链路检测帧
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		thread = new Thread(new LinkCheckSendThread(message, ctx.channel(), interval));
		thread.setDaemon(true);
		thread.setName(clientId + "#LinkCheckSendThread");
		thread.start();
		log.info(String.format("客户端[%s]启动连接检测发送线程", clientId));
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Message in = (Message) msg;
		try {
			if (!LinkCheckMessage.isLinkCheck(in)) {
				// 不是链路检测数据，转给下个handler
				ctx.fireChannelRead(msg);
				return;
			}
			log.info(String.format("客户端[%s]收到链路检测数据：%s", clientId, Crc8Util.formatHexString(in.toHexString())));
			if (!LinkCheckMessage.isOnline(in)) {
				// 返回退出在线,关闭通道
				ctx.fireExceptionCaught(new ClientException("远程服务返回退出"));
			} else {
				//返回保持在线，消息传给注册消息处理器
				ctx.fireChannelRead(msg);
			}
		} finally {
			// ByteBuf是一个引用计数对象，这个对象必须显示地调用release()方法来释放
			// or ((ByteBuf)msg).release();
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// log.info(String.format("客户端[%s]通道读取完毕！", clientId));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (null != cause) {
			log.error(String.format("客户端[%s]异常！", clientId), cause);
			thread.interrupt();
		}
		if (null != ctx) {
			ctx.close().sync();
		}
	}

}
