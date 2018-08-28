/**
 * 
 */
package com.xx.core.dto;

import java.util.List;

import com.xx.util.Crc8Util;

/**
 * @author lee 链路检测报文
 */
public class LinkCheckMessage extends ObjectMessage {

	/**
	 * 在线许可
	 */
	private int online;

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}


	@Override
	public List<Message> toMessage() {
		// 链路检测
		this.afn = 0x02;
        this.online = 0xf2;
		this.content = Crc8Util.byte2HexString((byte) (online & 0xff));
		return convertor.toByteMessage(this);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LinkCheckContent [online=");
		builder.append(online);
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}

}
