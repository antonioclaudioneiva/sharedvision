package br.ucsal.properties;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


public class PropertiesService {

	private static final String PROPERTIES_FILE_NAME = "sharedvision.properties";

	private static final String SYSTEM_NAME = "Shared Vision";

	private static final String SYSTEM_VERSION = "0.01B";

	private static String systemFullIdentification;

	private static Long screenRefreshTime;

	final static Logger logger = Logger.getLogger(PropertiesService.class);


	private static Properties properties;
	
	static {
		logger.info("Initializing properties service.");
		loadPropertiesFile();
		setProperties();
		logger.info("Properties service initialized.");
	}

	private static void loadPropertiesFile() {
		logger.debug("Loading properties file...");
		try {
			InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
					PROPERTIES_FILE_NAME);
			properties =  new Properties();
			properties.load(resourceAsStream);
			logger.debug("Properties file loaded.");
		} catch (Exception e) {
			logger.error("Load properties file failed: " + e.getMessage());
			properties = new Properties();
			logger.error("System running with empty properties file");
		}
	}

	public static String getProperty(PropertiesEnum propertiesEnum) {
		return properties.getProperty(propertiesEnum.getKey());
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static String getSystemFullIdentification() {
		return systemFullIdentification;
	}

	public static Long getScreenRefreshTime() {
		return screenRefreshTime;
	}

	private static void setProperties() {
		setSystemFullIdentification();
		setScreenRefreshTime();
	}

	private static void setScreenRefreshTime() {
		screenRefreshTime = getLongProperty(PropertiesEnum.SCREEN_REFRESH_TIME,
				PropertiesDefault.SCREEN_REFRESH_TIME_DEFAULT);
	}

	public static Boolean isLDAPAvaliable() {
		return getBooleanProperty(PropertiesEnum.LDAP_AVALIABLE, "false");
	}

	private static void setSystemFullIdentification() {
		systemFullIdentification = SYSTEM_NAME + " - version " + SYSTEM_VERSION;
	}

	private static Long getLongProperty(PropertiesEnum property, String defaultValue) {
		try {
			String value = properties.getProperty(property.getKey(), defaultValue);
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			logger.error("Get property " + property + " failed.");
		}
		return null;
	}

	private static Boolean getBooleanProperty(PropertiesEnum property, String defaultValue) {
		try {
			String value = properties.getProperty(property.getKey(), defaultValue);
			return Boolean.parseBoolean(value);
		} catch (NumberFormatException e) {
			logger.error("Get property " + property + " failed.");
		}
		return null;
	}
}
