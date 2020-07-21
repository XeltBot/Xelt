package me.theditor.xelt;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

	private static Dotenv dotenv;
	private static boolean sys;
	
	public static void setup() {
		try {
			dotenv = Dotenv.load();
			sys = false;
		} catch (Exception e) {
			sys = true;
		}
	}

	public static String get(String key) {
		String ret = "";
		if (!sys) {
			ret = dotenv.get(key.toUpperCase());
		} else {
			ret = System.getenv(key.toUpperCase());
		}
		
		return ret;
	}

}
