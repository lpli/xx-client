/**
 * 
 */
package com.xx.handler;

import com.xx.core.dto.Address;
import com.xx.core.dto.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * @author lee
 *
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Message msg = new Message();
		msg.setLength(0x08);
		msg.setSecStart((byte) 0x68);
		msg.setControl((byte) 0x81);
		msg.setAddress(new Address(new byte[] {0x01,0x21,0x26,0x00,0x01}));
		msg.setPayload(new String(new byte[] {0x02,(byte) 0xf2},CharsetUtil.UTF_8));
		msg.setCrc((byte) 0x00);
		ctx.writeAndFlush(msg);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		ByteBuf in = (ByteBuf) msg;
		try {
			// Do something with msg
			System.out.println("client get :" + in.toString(CharsetUtil.UTF_8));

			ctx.close();
		} finally {
			// ByteBuf是一个引用计数对象，这个对象必须显示地调用release()方法来释放
			// or ((ByteBuf)msg).release();
			ReferenceCountUtil.release(msg);
		}
	}
}
