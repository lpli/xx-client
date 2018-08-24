package com.xx.client;

public class SimpleTest {

	public static void main(String[] args) {
		String str = new String(new byte[] {0x02,(byte) 0xf2});
		for(byte s:str.getBytes()) {
			System.out.println(Integer.toHexString(0xff&s));
		}
	}

}
