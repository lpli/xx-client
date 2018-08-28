/**
 * 
 */
package com.xx.handler;

import java.util.concurrent.ConcurrentLinkedDeque;

import com.xx.core.dto.Message;
import com.xx.device.SyncFuture;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author lee
 *
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

	private SyncFuture<Message> responseFuture;

	

	public ClientHandler(SyncFuture<Message> responseFuture) {
		super();
		this.responseFuture = responseFuture;
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
			System.out.println("客户端接收数据："+in.toHexString());
			responseFuture.setResponse(in);
		} finally {
			// ByteBuf是一个引用计数对象，这个对象必须显示地调用release()方法来释放
			// or ((ByteBuf)msg).release();
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("通道读取完毕！");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (null != cause)
			cause.printStackTrace();
		if (null != ctx)
			ctx.close();
	}

}
