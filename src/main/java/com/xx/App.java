/**
 * 
 */
package com.xx;

import com.xx.core.dto.Address;
import com.xx.core.dto.Message;

/**
 * @author lee
 *
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.setLength((byte) 0x08);
		msg.setSecStart((byte) 0x68);
		msg.setControl((byte) 0xb1);
		msg.setAddress(new Address(new byte[] { 0x78, 0x21, 0x26, 0x00, 0x01 }));
		msg.setPayload(new byte[] { 0x02, (byte) 0xf2 });
		msg.setCrc((byte) 0xd7);

		Client client = new Client("tripnet.unilogger.cn", 10260);
		//等待启动
		while(true) {
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			client.sendMessage(msg);
		}
		

	}

}
