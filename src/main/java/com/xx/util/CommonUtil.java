/**
 * 
 */
package com.xx.util;

import java.net.InetAddress;
import java.net.NetworkInterface;

import com.xx.exception.ClientException;

/**
 * @author lee
 *
 */
public class CommonUtil {

	public static String getLocalMac() throws ClientException {
		StringBuffer sb = new StringBuffer("");
		try {
			InetAddress addr = InetAddress.getLocalHost();
			byte[] mac = NetworkInterface.getByInetAddress(addr).getHardwareAddress();

			for (int i = 0; i < mac.length; i++) {
				if (i != 0) {
					sb.append("-");
				}
				// 字节转换为整数
				int temp = mac[i] & 0xff;
				String str = Integer.toHexString(temp);
				if (str.length() == 1) {
					sb.append("0" + str);
				} else {
					sb.append(str);
				}
			}
		} catch (Exception e) {
			throw new ClientException(e);
		}

		return sb.toString();
	}

}
