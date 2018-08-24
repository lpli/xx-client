package com.xx.core.dto;

import java.io.Serializable;
import java.util.Arrays;

import com.xx.util.Crc8Util;

/**
 * 地址控制
 * 
 * @author lee
 *
 */
public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2549935717802576673L;

	/**
	 * 厂家编号 长度一个字节
	 */
	private byte productNo;

	/**
	 * 厂家密码 0x0000 占半个字节高位
	 */
	private byte productPwd;

	/**
	 * 生产日期-月 0x0000 占半个字节低位
	 */
	private byte month;

	/**
	 * 生产日期-年 占一个字节
	 */
	private byte year;

	/**
	 * 测站地址 长度两个字节
	 */
	private byte[] station;

	public byte getProductNo() {
		return productNo;
	}

	public void setProductNo(byte productNo) {
		this.productNo = productNo;
	}

	public byte getProductPwd() {
		return productPwd;
	}

	public void setProductPwd(byte productPwd) {
		this.productPwd = productPwd;
	}



	public byte getMonth() {
		return month;
	}

	public void setMonth(byte month) {
		this.month = month;
	}

	public byte getYear() {
		return year;
	}

	public void setYear(byte year) {
		this.year = year;
	}

	public byte[] getStation() {
		return station;
	}

	public void setStation(byte[] station) {
		this.station = station;
	}

	public byte[] getBytes() {
		byte pwd = (byte) (productPwd & 0xf0);
		byte m = (byte) (month & 0x0f);
		return new byte[] { productNo, (byte) (pwd | m), (byte) (year & 0xff), station[0], station[1] };
	}

	public Address() {
		super();
	}

	public Address(byte[] bytes) {
		if (bytes.length == 5) {
			this.productNo = bytes[0];
			this.productPwd = (byte) (bytes[1] & 0xf0);
			this.month = (byte) (bytes[1] & 0x0f);
			this.year = bytes[2];
			this.station = new byte[] { bytes[3], bytes[4] };
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Address [productNo=");
		builder.append(productNo);
		builder.append(", productPwd=");
		builder.append(productPwd);
		builder.append(", month=");
		builder.append(month);
		builder.append(", year=");
		builder.append(year);
		builder.append(", station=");
		builder.append(Arrays.toString(station));
		builder.append("]");
		return builder.toString();
	}

	
	public String toHexString() {
		StringBuilder builder = new StringBuilder();
		builder.append(Crc8Util.byte2HexString(getBytes()));
		return builder.toString();
	}
	
	
	

}
