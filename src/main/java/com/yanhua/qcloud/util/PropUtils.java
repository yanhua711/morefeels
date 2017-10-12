package com.yanhua.qcloud.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public final class PropUtils {

	private static final String CONGURATION_PROPERTIES_FILENAME = "configuration.properties";

	private static Configuration config;

	private PropUtils() {
	}

	static {
		try {
			final PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
			propertiesConfiguration.append(new PropertiesConfiguration(CONGURATION_PROPERTIES_FILENAME));
			config = propertiesConfiguration;
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description: 获取某个 properties 文件的值
	 * @param key
	 * @return
	 * @author yangyang.chen
	 * @date 2016年7月14日 下午3:55:44
	 */
	public static String getConfig(String key) {
		return config.getString(key);
	}

}
