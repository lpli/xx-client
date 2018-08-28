/**
 * 
 */
package com.xx;

import com.xx.core.dto.LinkCheckMessage;
import com.xx.core.dto.Message;
import com.xx.exception.ClientException;

/**
 * @author lee
 *
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		LinkCheckMessage msg = new LinkCheckMessage();
		msg.setDirect(1);
		msg.setDiv(0);
		msg.setFcb(3);
		msg.setFunctionCode(1);
		msg.setProductNo(120);
		msg.setProductPwd(6);
		msg.setMonth(7);
		msg.setYear(18);
		msg.setStation(1);
		try {
			Client client= new Client("tripnet.unilogger.cn", 10260);
			while (true) {
				try {
					Thread.sleep(2000L);
					Message message = client.sendMessage(msg);
					System.out.println(message.toHexString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (ClientException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 等待启动
		

	}

}
