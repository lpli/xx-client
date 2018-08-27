/**
 * 
 */
package com.xx.core.dto;

/**
 * @author lee
 *
 */
public interface FrameConstant {

	/**
	 * 最大帧长度
	 */
	public static final int MAX_LENGTH = 2048;
	/**
	 * 功能码长度
	 */
	public static final int FUNC_CODE_LENGTH = 1;

	/**
	 * 控制域长度
	 */
	public static final int CONTR_LENGTH = 1;

	/**
	 * 地址域长度
	 */
	public static final int ADDR_LENGTH = 5;

	public static final int MAX_CONTENT_LENGTH = FrameConstant.MAX_LENGTH - FrameConstant.FUNC_CODE_LENGTH
			- FrameConstant.CONTR_LENGTH - FrameConstant.ADDR_LENGTH;
}
