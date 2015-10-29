package com.utils;

public class Time {
	private static int timeZone = 2;
	private static long lastTime = 0;
	private static long currentTime = 0;
	
	public static void update() {
		lastTime = currentTime;
		currentTime = System.currentTimeMillis() + timeZone * 3600000;
	}
	
	public static int[] get() {
		int time[] = new int[7];
		time[6] = (int) (currentTime % 1000);
		time[5] = (int) ((currentTime / 1000) % 60);
		time[4] = (int) ((currentTime / 60000) % 60);
		time[3] = (int) ((currentTime / 3600000) % 24);
		time[2] = (int) (currentTime / 3600000 / 24);
		
		time[0] = 2000 + ((time[2] - 2 * 365) / (4 * 365 + 1) - 7) * 4;
		time[1] = 1;
		time[2] = (time[2] - 2 * 365) % (4 * 365 + 1);
		
		int bis = 1;
		if (time[2] > 366) {
			time[0] += (time[2] - 366) / 365 + 1;
			time[2] = (time[2] - 366) % 365;
			bis = 0;
		}
		//January
		if (time[2] >= 31) {
			time[2] -= 31;
			time[1]++;
		}
		//February
		if (time[2] >= 28 + bis) {
			time[2] -= 28;
			time[1]++;
		}
		//March
		if (time[2] >= 31) {
			time[2] -= 31;
			time[1]++;
		}
		//April
		if (time[2] >= 30) {
			time[2] -= 30;
			time[1]++;
		}
		//May
		if (time[2] >= 31) {
			time[2] -= 31;
			time[1]++;
		}
		//June
		if (time[2] >= 30) {
			time[2] -= 30;
			time[1]++;
		}
		//July
		if (time[2] >= 31) {
			time[2] -= 31;
			time[1]++;
		}
		//August
		if (time[2] >= 31) {
			time[2] -= 31;
			time[1]++;
		}
		//September
		if (time[2] >= 30) {
			time[2] -= 30;
			time[1]++;
		}
		//October
		if (time[2] >= 31) {
			time[2] -= 31;
			time[1]++;
		}
		//November
		if (time[2] >= 30) {
			time[2] -= 30;
			time[1]++;
		}
		//December - as it is
		time[2]++;
		
		return time;
	}
	
	public static int getDelta() {
		return (int) (currentTime - lastTime);
	}
}
