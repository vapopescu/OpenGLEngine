package com.utils;

public class Utils {
	public static int byteToInt(byte n) {
		int x;
		if (n >= 0) {
			x = n;
		} else {
			x = n + 256;
		}
		return x;
	}
}
