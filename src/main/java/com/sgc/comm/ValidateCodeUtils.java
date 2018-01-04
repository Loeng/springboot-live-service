package com.sgc.comm;

import java.util.Random;

public class ValidateCodeUtils {
	public static String getRandNum() {
		String sRand = "";
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
		}
		return sRand;
	}
}
