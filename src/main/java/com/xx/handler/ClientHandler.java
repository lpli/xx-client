/**
 *
 */
package com.xx.handler;

import com.xx.core.dto.LinkCheckMessage;
import com.xx.core.dto.Message;
import com.xx.device.SyncFuture;
import com.xx.util.Crc8Util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author lee
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

	private static final Log log = LogFactory.getLog(ClientHandler.class);

	private SyncFuture<Message> responseFuture;

	private String clientId;

	public ClientHandler(SyncFuture<Message> responseFuture, String clientId) {
		super();
		this.responseFuture = responseFuture;
		this.clientId = clientId;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 启动后发送心跳数据
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		Message in = (Message) msg;
		try {
			log.info(String.format("客户端[%s]收到数据：%s", clientId, Crc8Util.formatHexString(in.toHexString())));
			responseFuture.setResponse(in);
		} finally {
			// ByteBuf是一个引用计数对象，这个对象必须显示地调用release()方法来释放
			// or ((ByteBuf)msg).release();
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		log.info(String.format("客户端[%s]通道读取完毕！", clientId));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (null != cause) {
			log.info(String.format("客户端[%s]异常！", clientId), cause);
		}
		if (null != ctx) {
			ctx.close();
		}
	}

}
