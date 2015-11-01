package com.deer.core.utils.generators;

import java.util.Date;
import java.util.Random;

public class GenerateRandCN {
	public String getChinese(long seed) throws Exception {
		String str = null;
		int highPos, lowPos;
		seed = new Date().getTime();
		Random random = new Random(seed);
		highPos = (176 + Math.abs(random.nextInt(39)));
		lowPos = 161 + Math.abs(random.nextInt(93));
		byte[] b = new byte[2];
		b[0] = (new Integer(highPos)).byteValue();
		b[1] = (new Integer(lowPos)).byteValue();
		str = new String(b, "utf-8");
		return str;
	}

	public static String get300Chinese() throws Exception {
		GenerateRandCN ch = new GenerateRandCN();
		String str = "";
		for (int i = 300; i > 0; i--) {
			str = str + ch.getChinese(i);

		}
		System.out.println(str);
		return str;
	}

	public static void main(String[] args) throws Exception {
		System.out.println((char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1))));
	}
}
