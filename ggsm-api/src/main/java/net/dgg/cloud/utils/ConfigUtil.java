package net.dgg.cloud.utils;

import java.io.IOException;
import java.util.Properties;

public class ConfigUtil {
	
	private static Properties properties;
	
	static{
		init();
	}
	
	public static void init(){
		properties = new Properties();
		try {
			properties.load(ConfigUtil.class.getClassLoader().getResourceAsStream("constants.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getProperties(String name){
		return properties.getProperty(name);
	}
	
}
