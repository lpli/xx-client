/*
 * Copyright (c) 111.com Inc. All rights  reserved.
 *
 *
 */

package com.xx.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author liliping
 * @Date 2018/8/22
 **/
public class Crc8Util {

	public enum CrcType {
		HIGH(1, "高位"), LOW(0, "低位");
		private int code;
		private String desc;

		CrcType(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}

	public static byte[] crc8_high_tab = new byte[] { (byte) 0, (byte) -128, (byte) -27, (byte) 101, (byte) 47,
			(byte) -81, (byte) -54, (byte) 74, (byte) 94, (byte) -34, (byte) -69, (byte) 59, (byte) 113, (byte) -15,
			(byte) -108, (byte) 20, (byte) -68, (byte) 60, (byte) 89, (byte) -39, (byte) -109, (byte) 19, (byte) 118,
			(byte) -10, (byte) -30, (byte) 98, (byte) 7, (byte) -121, (byte) -51, (byte) 77, (byte) 40, (byte) -88,
			(byte) -99, (byte) 29, (byte) 120, (byte) -8, (byte) -78, (byte) 50, (byte) 87, (byte) -41, (byte) -61,
			(byte) 67, (byte) 38, (byte) -90, (byte) -20, (byte) 108, (byte) 9, (byte) -119, (byte) 33, (byte) -95,
			(byte) -60, (byte) 68, (byte) 14, (byte) -114, (byte) -21, (byte) 107, (byte) 127, (byte) -1, (byte) -102,
			(byte) 26, (byte) 80, (byte) -48, (byte) -75, (byte) 53, (byte) -33, (byte) 95, (byte) 58, (byte) -70,
			(byte) -16, (byte) 112, (byte) 21, (byte) -107, (byte) -127, (byte) 1, (byte) 100, (byte) -28, (byte) -82,
			(byte) 46, (byte) 75, (byte) -53, (byte) 99, (byte) -29, (byte) -122, (byte) 6, (byte) 76, (byte) -52,
			(byte) -87, (byte) 41, (byte) 61, (byte) -67, (byte) -40, (byte) 88, (byte) 18, (byte) -110, (byte) -9,
			(byte) 119, (byte) 66, (byte) -62, (byte) -89, (byte) 39, (byte) 109, (byte) -19, (byte) -120, (byte) 8,
			(byte) 28, (byte) -100, (byte) -7, (byte) 121, (byte) 51, (byte) -77, (byte) -42, (byte) 86, (byte) -2,
			(byte) 126, (byte) 27, (byte) -101, (byte) -47, (byte) 81, (byte) 52, (byte) -76, (byte) -96, (byte) 32,
			(byte) 69, (byte) -59, (byte) -113, (byte) 15, (byte) 106, (byte) -22, (byte) 91, (byte) -37, (byte) -66,
			(byte) 62, (byte) 116, (byte) -12, (byte) -111, (byte) 17, (byte) 5, (byte) -123, (byte) -32, (byte) 96,
			(byte) 42, (byte) -86, (byte) -49, (byte) 79, (byte) -25, (byte) 103, (byte) 2, (byte) -126, (byte) -56,
			(byte) 72, (byte) 45, (byte) -83, (byte) -71, (byte) 57, (byte) 92, (byte) -36, (byte) -106, (byte) 22,
			(byte) 115, (byte) -13, (byte) -58, (byte) 70, (byte) 35, (byte) -93, (byte) -23, (byte) 105, (byte) 12,
			(byte) -116, (byte) -104, (byte) 24, (byte) 125, (byte) -3, (byte) -73, (byte) 55, (byte) 82, (byte) -46,
			(byte) 122, (byte) -6, (byte) -97, (byte) 31, (byte) 85, (byte) -43, (byte) -80, (byte) 48, (byte) 36,
			(byte) -92, (byte) -63, (byte) 65, (byte) 11, (byte) -117, (byte) -18, (byte) 110, (byte) -124, (byte) 4,
			(byte) 97, (byte) -31, (byte) -85, (byte) 43, (byte) 78, (byte) -50, (byte) -38, (byte) 90, (byte) 63,
			(byte) -65, (byte) -11, (byte) 117, (byte) 16, (byte) -112, (byte) 56, (byte) -72, (byte) -35, (byte) 93,
			(byte) 23, (byte) -105, (byte) -14, (byte) 114, (byte) 102, (byte) -26, (byte) -125, (byte) 3, (byte) 73,
			(byte) -55, (byte) -84, (byte) 44, (byte) 25, (byte) -103, (byte) -4, (byte) 124, (byte) 54, (byte) -74,
			(byte) -45, (byte) 83, (byte) 71, (byte) -57, (byte) -94, (byte) 34, (byte) 104, (byte) -24, (byte) -115,
			(byte) 13, (byte) -91, (byte) 37, (byte) 64, (byte) -64, (byte) -118, (byte) 10, (byte) 111, (byte) -17,
			(byte) -5, (byte) 123, (byte) 30, (byte) -98, (byte) -44, (byte) 84, (byte) 49 };

	public static byte[] crc8_low_tab = new byte[] { (byte) 0, (byte) -30, (byte) 15, (byte) -19, (byte) 30, (byte) -4,
			(byte) 17, (byte) -13, (byte) -9, (byte) 21, (byte) -8, (byte) 26, (byte) -23, (byte) 11, (byte) -26,
			(byte) 4, (byte) -18, (byte) 12, (byte) -31, (byte) 3, (byte) -16, (byte) 18, (byte) -1, (byte) 29,
			(byte) 25, (byte) -5, (byte) 22, (byte) -12, (byte) 7, (byte) -27, (byte) 8, (byte) -22, (byte) 23,
			(byte) -11, (byte) 24, (byte) -6, (byte) 9, (byte) -21, (byte) 6, (byte) -28, (byte) -32, (byte) 2,
			(byte) -17, (byte) 13, (byte) -2, (byte) 28, (byte) -15, (byte) 19, (byte) -7, (byte) 27, (byte) -10,
			(byte) 20, (byte) -25, (byte) 5, (byte) -24, (byte) 10, (byte) 14, (byte) -20, (byte) 1, (byte) -29,
			(byte) 16, (byte) -14, (byte) 31, (byte) -3, (byte) -27, (byte) 7, (byte) -22, (byte) 8, (byte) -5,
			(byte) 25, (byte) -12, (byte) 22, (byte) 18, (byte) -16, (byte) 29, (byte) -1, (byte) 12, (byte) -18,
			(byte) 3, (byte) -31, (byte) 11, (byte) -23, (byte) 4, (byte) -26, (byte) 21, (byte) -9, (byte) 26,
			(byte) -8, (byte) -4, (byte) 30, (byte) -13, (byte) 17, (byte) -30, (byte) 0, (byte) -19, (byte) 15,
			(byte) -14, (byte) 16, (byte) -3, (byte) 31, (byte) -20, (byte) 14, (byte) -29, (byte) 1, (byte) 5,
			(byte) -25, (byte) 10, (byte) -24, (byte) 27, (byte) -7, (byte) 20, (byte) -10, (byte) 28, (byte) -2,
			(byte) 19, (byte) -15, (byte) 2, (byte) -32, (byte) 13, (byte) -17, (byte) -21, (byte) 9, (byte) -28,
			(byte) 6, (byte) -11, (byte) 23, (byte) -6, (byte) 24, (byte) -1, (byte) 29, (byte) -16, (byte) 18,
			(byte) -31, (byte) 3, (byte) -18, (byte) 12, (byte) 8, (byte) -22, (byte) 7, (byte) -27, (byte) 22,
			(byte) -12, (byte) 25, (byte) -5, (byte) 17, (byte) -13, (byte) 30, (byte) -4, (byte) 15, (byte) -19,
			(byte) 0, (byte) -30, (byte) -26, (byte) 4, (byte) -23, (byte) 11, (byte) -8, (byte) 26, (byte) -9,
			(byte) 21, (byte) -24, (byte) 10, (byte) -25, (byte) 5, (byte) -10, (byte) 20, (byte) -7, (byte) 27,
			(byte) 31, (byte) -3, (byte) 16, (byte) -14, (byte) 1, (byte) -29, (byte) 14, (byte) -20, (byte) 6,
			(byte) -28, (byte) 9, (byte) -21, (byte) 24, (byte) -6, (byte) 23, (byte) -11, (byte) -15, (byte) 19,
			(byte) -2, (byte) 28, (byte) -17, (byte) 13, (byte) -32, (byte) 2, (byte) 26, (byte) -8, (byte) 21,
			(byte) -9, (byte) 4, (byte) -26, (byte) 11, (byte) -23, (byte) -19, (byte) 15, (byte) -30, (byte) 0,
			(byte) -13, (byte) 17, (byte) -4, (byte) 30, (byte) -12, (byte) 22, (byte) -5, (byte) 25, (byte) -22,
			(byte) 8, (byte) -27, (byte) 7, (byte) 3, (byte) -31, (byte) 12, (byte) -18, (byte) 29, (byte) -1,
			(byte) 18, (byte) -16, (byte) 13, (byte) -17, (byte) 2, (byte) -32, (byte) 19, (byte) -15, (byte) 28,
			(byte) -2, (byte) -6, (byte) 24, (byte) -11, (byte) 23, (byte) -28, (byte) 6, (byte) -21, (byte) 9,
			(byte) -29, (byte) 1, (byte) -20, (byte) 14, (byte) -3, (byte) 31, (byte) -14, (byte) 16, (byte) 20,
			(byte) -10, (byte) 27, (byte) -7, (byte) 10, (byte) -24, (byte) 5 };

	/**
	 * 高位单字节crc8
	 *
	 * @param ch
	 * @return
	 */
	public static int singleByteHighCrc(byte ch) {
		byte crc = ch;
		for (int i = 7; i > 0; --i) {
			if ((crc & 0x80) != 0) {
				crc = (byte) ((crc << 1) ^ 0xE5);
			} else {
				crc = (byte) (crc << 1);
			}
		}
		return crc;
	}

	/**
	 * 低位单字节crc8
	 *
	 * @param ch
	 * @return
	 */
	public static int singleByteLowCrc(byte ch) {
		byte crc = ch;
		for (int i = 7; i > 0; --i) {
			if ((crc & 0x01) != 0) {
				crc = (byte) ((crc >> 1) ^ 0xE5);
			} else {
				crc = (byte) (crc >> 1);
			}
		}
		return crc;
	}

	/**
	 * 获取crc8�?
	 *
	 * @param type
	 *            0 低位�?1高位
	 * @return
	 */
	public static String getCrc8Tab(CrcType type) {
		StringBuilder strb = new StringBuilder();
		for (int i = 0; i < 0xff; i++) {
			if ((i % 16) == 0) {
				strb.append("\n");
			}
			if (type == CrcType.HIGH) {
				strb.append("(byte)").append(singleByteHighCrc((byte) (i & 0xff))).append(",");
			} else {
				strb.append("(byte)").append(singleByteLowCrc((byte) (i & 0xff))).append(",");
			}

		}
		return strb.substring(0, strb.length() - 1);
	}

	/**
	 * 获取单字�? crc校验�?
	 * 
	 * @param data
	 *            数据
	 * @param type
	 *            类型
	 * @return
	 */
	public static byte getCrc(byte[] data, CrcType type) {
		byte crc = 0x00;
		for (byte d : data) {
			if (type == CrcType.HIGH) {
				crc = crc8_high_tab[0xff & (crc ^ d)];
			} else {
				crc = crc8_low_tab[0xff & (crc ^ d)];
			}
		}
		return crc;
	}

	/**
	 * 字符转换为字�?
	 *
	 * @param c
	 * @return
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 16进制字符串转字节数组
	 *
	 * @param hex
	 * @return
	 */
	public static byte[] hexString2Bytes(String hex) {
		if ((hex == null) || (hex.equals(""))) {
			return null;
		} else if (hex.length() % 2 != 0) {
			return null;
		} else {
			hex = hex.toUpperCase();
			int len = hex.length() / 2;
			byte[] b = new byte[len];
			char[] hc = hex.toCharArray();
			for (int i = 0; i < len; i++) {
				int p = 2 * i;
				b[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
			}
			return b;
		}

	}

	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	public static String byte2HexString(byte[] data) {
		char[] buf = new char[data.length * 2];
		int a = 0;
		int index = 0;
		for (byte b : data) {
			// 使用除与取余进行转换
			if (b < 0) {
				a = 256 + b;
			} else {
				a = b;
			}

			buf[index++] = HEX_CHAR[a / 16];
			buf[index++] = HEX_CHAR[a % 16];
		}


		return new String(buf);
	}

	public static String byte2HexString(byte data) {
		return byte2HexString(new byte[] { data });
	}

	public static byte[] float2byte(float f) {

		// 把float转换为byte[]
		int fbit = Float.floatToIntBits(f);

		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (fbit >> (24 - i * 8));
		}

		// 翻转数组
		int len = b.length;
		// 建立一个与源数组元素类型相同的数组
		byte[] dest = new byte[len];
		// 为了防止修改源数组，将源数组拷贝一份副本
		System.arraycopy(b, 0, dest, 0, len);
		byte temp;
		// 将顺位第i个与倒数第i个交换
		for (int i = 0; i < len / 2; ++i) {
			temp = dest[i];
			dest[i] = dest[len - i - 1];
			dest[len - i - 1] = temp;
		}

		return dest;
	}
	
	

	public static String formatHexString(String hexStr){
		if(hexStr.length() % 2 != 0){
			System.out.println("数据不正确");
			return null;
		}
		int splitLen = 2;
		int n = hexStr.length()/2;
		List<String> list = new ArrayList<String>(n);
		for(int i=0;i<n;i++){
			list.add(hexStr.substring(i*splitLen,(i+1)*splitLen));
		}
		return StringUtils.join(list," ");
	}

	public static void main(String[] args) {
		String hexStr = "00786126000102f2";

		System.out.println(Integer.toHexString(0xff & getCrc(hexString2Bytes(hexStr), CrcType.LOW)));
		System.out.println(formatHexString(byte2HexString(float2byte(324f))));;

	}
}
