/**
 * 
 */
package com.xx;

import java.util.concurrent.ExecutionException;

import com.xx.core.dto.Message;
import com.xx.core.dto.RealtimeMessage;
import com.xx.core.dto.RegisterMessage;
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

		try {
			Client client = new Client("tripnet.unilogger.cn", 10260, "test", 120, 6, 18, 7, 1, 60,
					"00000000000000000018621818050001");
			client.connect();
			while (true) {
				Thread.sleep(3000L);
//				RealtimeMessage message = new RealtimeMessage();
//				message.setChannelNum(1);
//				message.setData(new float[] { 10.2f });
				
//				RegisterMessage message = new RegisterMessage();
//				message.setSerial("00000000000000000018621818050001");
//				Message msg = client.sendMessage(message);
//				if (msg != null) {
//					System.out.println("返回" + msg.toHexString());
//				}
			}
		} catch (ClientException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
