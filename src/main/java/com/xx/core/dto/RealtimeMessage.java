package com.xx.core.dto;

import java.util.List;

import com.xx.util.Crc8Util;

public class RealtimeMessage extends ObjectMessage {

	/**
	 * 通道数
	 */
	private int channelNum;

	private float[] data;

	public int getChannelNum() {
		return channelNum;
	}

	public void setChannelNum(int channelNum) {
		this.channelNum = channelNum;
	}

	public float[] getData() {
		return data;
	}

	public void setData(float[] data) {
		this.data = data;
	}

	@Override
	public List<Message> toMessage() {
		this.afn = 0x66;
		StringBuilder str = new StringBuilder();
		str.append(Crc8Util.byte2HexString((byte) (this.channelNum & 0xff)));
		for (float f:data) {
			str.append(Crc8Util.byte2HexString(Crc8Util.float2byte(f)));
		}
		this.content = str.toString();
		return convertor.toByteMessage(this);
	}

}
