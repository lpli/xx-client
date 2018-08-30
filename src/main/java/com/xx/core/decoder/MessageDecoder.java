/**
 * 
 */
package com.xx.core.decoder;

import java.util.Arrays;
import java.util.List;

import com.xx.core.dto.Address;
import com.xx.core.dto.Message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 
 * 数据解码器
 * @author lee
 *
 */
public class MessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if(in.readableBytes() < 11) {
			return;
		}
		Message message = new Message();
		in.readByte();
		byte len = in.readByte();
		message.setLength(len);
		int length = (0xff & len);
		
		byte secStart = in.readByte();
		message.setSecStart(secStart);
		if (secStart != 0x68) {
			//长度
			length = ((secStart & 0x07) << 8) + length;
		}
		
		byte[] data = new byte[length];
		in.readBytes(data);
//		byte expectCrc = Crc8Util.getCrc(data, Crc8Util.CrcType.HIGH);
		byte crc = in.readByte();
		//结束标志
		in.readByte();
		/*if(expectCrc != crc) {
			System.out.println("crc 校验失败:"+Integer.toHexString(expectCrc&0xff));
		}*/
		byte control = data[0];
		message.setControl(control);
		byte[] address,payload;
		if((control & 0x40) !=0) {
			byte divs = data[1];
			message.setDivs(divs);
			address = Arrays.copyOfRange(data, 2, 7);
			payload = Arrays.copyOfRange(data, 7, length);
		}else {
			address = Arrays.copyOfRange(data, 1, 6);
			payload = Arrays.copyOfRange(data, 6, length);
		}
		
		message.setAddress(new Address(address));
		message.setPayload(payload);
		message.setCrc(crc);
		out.add(message);		
	}

}
