/**
 *
 */
package com.xx.core.dto;

import java.util.List;

/**
 * @author lee 链路检测报文
 */
public class RegisterMessage extends ObjectMessage {

	/**
	 * 序列号16： 0055101805011801 ，00 00 00 00 00 00 00 00 00 55 10 18 05 01 18 01
	 * 产品类型2：3128 ，0c 38 软件型号2： 5510，15 86 软件版本2： 1808， 07 10 硬件型号2： 5510，15 86
	 * 硬件版本2： 1712，06 b0 协议版本2： 126， 00 7e
	 */
	private String serial;
	private int productType;
	private int softModel;
	private int softVer;
	private int hardModel;
	private int hardVer;
	private int protocal;

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public int getProductType() {
		return productType;
	}

	public void setProductType(int productType) {
		this.productType = productType;
	}

	public int getSoftModel() {
		return softModel;
	}

	public void setSoftModel(int softModel) {
		this.softModel = softModel;
	}

	public int getSoftVer() {
		return softVer;
	}

	public void setSoftVer(int softVer) {
		this.softVer = softVer;
	}

	public int getHardModel() {
		return hardModel;
	}

	public void setHardModel(int hardModel) {
		this.hardModel = hardModel;
	}

	public int getHardVer() {
		return hardVer;
	}

	public void setHardVer(int hardVer) {
		this.hardVer = hardVer;
	}

	public int getProtocal() {
		return protocal;
	}

	public void setProtocal(int protocal) {
		this.protocal = protocal;
	}

	@Override
	public List<Message> toMessage() {
		// 链路检测
		this.afn = 0x65;

		// 数据域
		this.productType = 3128;
		this.softModel = 5510;
		this.softVer = 1808;
		this.hardModel = 5510;
		this.hardVer = 1712;
		this.protocal = 126;
		StringBuilder str = new StringBuilder();
		str.append(this.serial);
		str.append(String.format("%04x", this.productType));
		str.append(String.format("%04x", this.softModel));
		str.append(String.format("%04x", this.softVer));
		str.append(String.format("%04x", this.hardModel));
		str.append(String.format("%04x", this.hardVer));
		str.append(String.format("%04x", this.protocal));
		this.content = str.toString();
		return convertor.toByteMessage(this);
	}

	public static boolean isRegister(Message message) {
		byte[] payload = message.getPayload();
		return payload.length > 0 && ((payload[0] & 0xff) == 0x65);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LinkCheckContent [serial=");
		builder.append(serial);
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}

}
