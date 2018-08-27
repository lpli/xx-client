/**
 * 
 */
package com.xx.core.dto;

import java.util.List;

import com.xx.core.convertor.impl.DefaultMessageConvertor;

/**
 * @author lee
 *
 */
public abstract class ObjectMessage {
	protected DefaultMessageConvertor convertor = new DefaultMessageConvertor();
	
	/**
	 * 传输方向 0：下行；1：上行
	 */
	protected int direct;
	
	/**
	 * 是否拆分帧
	 */
	protected int div;
	
	/**
	 * 帧位计数
	 */
	protected int fcb;
	
	/**
	 * 功能码
	 */
	private int functionCode;
	
	/**
	 * 厂家编码
	 */
	protected int productNo;
	
	/**
	 * 厂家密码
	 */
	protected int productPwd;
	
	/**
	 * 月
	 */
	protected int month;
	
	/**
	 * 年
	 */
	protected int year;
	/**
	 * 测站
	 */
	protected int station;
	
	/**
	 * 功能码
	 */
	protected int afn;
	
	/**
	 * 消息内容 十六进制字符串
	 */
	protected String content;
	
	




	public int getProductNo() {
		return productNo;
	}

	public void setProductNo(int productNo) {
		this.productNo = productNo;
	}

	public int getProductPwd() {
		return productPwd;
	}

	public void setProductPwd(int productPwd) {
		this.productPwd = productPwd;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getStation() {
		return station;
	}

	public void setStation(int station) {
		this.station = station;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

	public int getDiv() {
		return div;
	}

	public void setDiv(int div) {
		this.div = div;
	}

	public int getFcb() {
		return fcb;
	}

	public void setFcb(int fcb) {
		this.fcb = fcb;
	}

	public int getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(int functionCode) {
		this.functionCode = functionCode;
	}
	
	

	public int getAfn() {
		return afn;
	}

	public void setAfn(int afn) {
		this.afn = afn;
	}

	
	/**
	 * 转换为 发送数据格式数据
	 * @return
	 */
	public abstract List<Message> toMessage();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ObjectMessage [direct=");
		builder.append(direct);
		builder.append(", div=");
		builder.append(div);
		builder.append(", fcb=");
		builder.append(fcb);
		builder.append(", functionCode=");
		builder.append(functionCode);
		builder.append(", productNo=");
		builder.append(productNo);
		builder.append(", productPwd=");
		builder.append(productPwd);
		builder.append(", month=");
		builder.append(month);
		builder.append(", year=");
		builder.append(year);
		builder.append(", station=");
		builder.append(station);
		builder.append(", afn=");
		builder.append(afn);
		builder.append(", content=");
		builder.append(content);
		builder.append("]");
		return builder.toString();
	}
	
	

}
