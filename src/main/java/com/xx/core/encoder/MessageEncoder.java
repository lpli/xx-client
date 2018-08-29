/**
 * 
 */
package com.xx.core.encoder;

import com.xx.core.dto.Address;
import com.xx.core.dto.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 数据编码器
 * @author lee
 *
 */
public class MessageEncoder extends MessageToByteEncoder<Message>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		out.writeByte(msg.getStart());
		out.writeByte(msg.getLength());
		out.writeByte(msg.getSecStart());
		byte control = msg.getControl();
		out.writeByte(control);
		if((control & 0x40) !=0) {
			out.writeByte(msg.getDivs());
		}
		Address address = msg.getAddress();
		out.writeBytes(address.getBytes());
		out.writeBytes(msg.getPayload());
		out.writeByte(msg.getCrc());
		out.writeByte(msg.getEnd());
		ctx.flush();
	}

}
