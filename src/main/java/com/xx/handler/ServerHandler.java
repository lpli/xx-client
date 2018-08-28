/**
 * 
 */
package com.xx.handler;

import com.xx.core.dto.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author lee
 *
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

	// 每当从客户端收到新的数据时，这个方法会在收到消息时被调用

	// exceptionCaught()事件处理方法是当出现Throwable对象才会被调用
	// 当Netty由于IO错误或者处理器在处理事件时抛出的异常时
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			Message in = (Message) msg;
			// Do something with msg
			System.out.println("server get :" + in.toHexString());

		} finally {
			// ByteBuf是一个引用计数对象，这个对象必须显示地调用release()方法来释放
			// or ((ByteBuf)msg).release();
			ReferenceCountUtil.release(msg);
		}
	}

}
