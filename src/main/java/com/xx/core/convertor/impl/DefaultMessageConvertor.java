/**
 * 
 */
package com.xx.core.convertor.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.xx.core.convertor.MessageConvertor;
import com.xx.core.dto.Address;
import com.xx.core.dto.FrameConstant;
import com.xx.core.dto.Message;
import com.xx.core.dto.ObjectMessage;
import com.xx.util.Crc8Util;

/**
 * 
 * 默认消息转换器
 * 
 * @author lee
 *
 */
public class DefaultMessageConvertor implements MessageConvertor<ObjectMessage> {

	@Override
	public List<Message> toByteMessage(ObjectMessage t) {
		List<Message> list = new ArrayList<Message>();
		int contentLen;
		if (t.getContent() == null) {
			contentLen = 0;
			return list;
		}
		byte[] content = Crc8Util.hexString2Bytes(t.getContent());
		contentLen = content.length;
		if (contentLen > FrameConstant.MAX_CONTENT_LENGTH) {
			// 消息
			System.out.println("消息长度过长");

		} else {
			byte control = (byte) ((t.getDirect() << 7 & 0x80) | (t.getDiv() << 6 & 0x40) | (t.getFcb() << 4 & 0x30)
					| (t.getFunctionCode() & 0x0f));

			byte pNo = (byte) (t.getProductNo() & 0xff);
			byte pwd = (byte) ((t.getProductPwd() << 4 & 0xf0) | (t.getMonth() & 0x0f));
			byte y = (byte) (t.getYear() & 0xff);

			byte[] station = new byte[2];
			station[0] = (byte) (t.getStation() & 0xff);
			station[1] = (byte) (t.getStation() >> 8 & 0xff);
			Message msg = new Message();

			msg.setSecStart((byte) 0x68);
			msg.setControl((byte) control);
			msg.setAddress(new Address(new byte[] { pNo, pwd, y, station[1], station[0] }));

			byte[] con = Arrays.copyOf(new byte[] { (byte) (t.getAfn() & 0xff) }, content.length + 1);
			System.arraycopy(content, 0, con, 1, content.length);
			int len = 6 + content.length + 1;
			msg.setLength((byte) (len & 0xff));
			msg.setPayload(con);
			// TODO crc校验暂时先不计算
			msg.setCrc((byte) 0x00);
			list.add(msg);
		}

		return list;
	}

	/*@Override
	public ObjectMessage toObjectMessage(Message message) {
		ObjectMessage objectMessage = new ObjectMessage();
		byte control = message.getControl();

		objectMessage.setDirect(control >> 7 & 0x01);
		objectMessage.setDiv(control >> 6 & 0x01);
		objectMessage.setFcb(control >> 4 & 0x03);
		objectMessage.setFunctionCode(control & 0x0f);
		byte[] addr = message.getAddress().getBytes();

		int productNo = addr[0] & 0x000000ff;
		int productPwd = (addr[1] & 0x000000f0) >> 4;
		int month = addr[1] & 0x0000000f;
		int year = addr[2] & 0x000000ff;
		int station = (addr[3] & 0x0000ff00) | (addr[4] & 0x000000ff);
		objectMessage.setProductNo(productNo);
		objectMessage.setProductPwd(productPwd);
		objectMessage.setMonth(month);
		objectMessage.setYear(year);
		objectMessage.setStation(station);
		byte[] payload = message.getPayload();
		objectMessage.setAfn(payload[0]);
		objectMessage.setContent(Crc8Util.byte2HexString(Arrays.copyOfRange(payload, 1, payload.length)));
		return objectMessage;
	}*/

	public static void main(String[] args) {
		// productNo 120

		// pwd 6
		// month 1
		// year 18;
		// station 1
		// ctrl 10110001
		// afn 66H
		// content 02000000000000a243
		


	}

}
