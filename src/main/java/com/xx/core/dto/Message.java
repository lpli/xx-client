package com.xx.core.dto;

import java.io.Serializable;

/**
 * 消息结构
 * @author lee
 *
 */
public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1645001433425380494L;

	/**
	 * 起始字符,占一个字节 默认值 0x68
	 */
	private byte start = 0x68;
	
	/**
	 * 消息长度 =  length(control) + length(address)+length(payload)
	 * 
	 * 默认一个字节，2^8 = 256-1=255 
	 * 当消息长度超过255字节时，使用secStart 的低三位 作为长度L的高位拓展
	 */
	private int length;
	
	/**
	 * 长度一个字节
	 * 默认为0x68  作为L的高位拓展后 值会变动
	 */
	private byte secStart = 0x68;
	
	
	/**
	 * 控制域 ,长度一个字节
	 */
	private byte control;
	
	
	/**
	 * 拆分帧计数
	 * 长度一个字节
	 */
	private byte divs;
	
	/**
	 * 地址
	 * 长度为5个字节
	 */
	private Address address;
	
	/**
	 * 用户数据
	 */
	private String payload;
	
	/**
	 * 帧crc校验值
	 * 
	 * 为 control+divs+address+payload 的crc校验值
	 */
	private byte crc;
	/**
	 * 结束字符
	 * 长度一个字节
	 */
	private byte end = 0x16;
	public byte getStart() {
		return start;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public byte getSecStart() {
		return secStart;
	}
	public void setSecStart(byte secStart) {
		this.secStart = secStart;
	}
	public byte getControl() {
		return control;
	}
	public void setControl(byte control) {
		this.control = control;
	}
	public byte getDivs() {
		return divs;
	}
	public void setDivs(byte divs) {
		this.divs = divs;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public byte getCrc() {
		return crc;
	}
	public void setCrc(byte crc) {
		this.crc = crc;
	}
	public byte getEnd() {
		return end;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Message [start=");
		builder.append(start);
		builder.append(", length=");
		builder.append(length);
		builder.append(", secStart=");
		builder.append(secStart);
		builder.append(", control=");
		builder.append(control);
		builder.append(", divs=");
		builder.append(divs);
		builder.append(", address=");
		builder.append(address);
		builder.append(", payload=");
		builder.append(payload);
		builder.append(", crc=");
		builder.append(crc);
		builder.append(", end=");
		builder.append(end);
		builder.append("]");
		return builder.toString();
	}
	
	
}
