package com.ck.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigHelper {
	private static Configuration config = null;
	static {
		try {
			config = new PropertiesConfiguration("config.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to load config.properties");
		}
	}
	public static String getProperty(String str) {
		return config.getString(str);
	}

}
