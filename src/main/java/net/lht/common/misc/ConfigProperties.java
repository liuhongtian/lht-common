package net.lht.common.misc;

import java.io.InputStream;
import java.util.Properties;

//import org.apache.log4j.Logger;

/**
 * config.properties
 * 
 * @author liuhongtian
 * 
 */
public class ConfigProperties extends Properties {

	private static final long serialVersionUID = 7159324916694657645L;

	//private static final Logger log = Logger.getLogger(ConfigProperties.class);

	private static ConfigProperties instance;

	protected ConfigProperties() {
		super();
	}

	public static ConfigProperties instance() {
		if (instance == null) {
			synchronized (ConfigProperties.class) {
				if (instance == null) {
					instance = new ConfigProperties();
					try {
						InputStream is = instance.getClass().getClassLoader()
								.getResourceAsStream("application.properties");
						instance.load(is);
					} catch (Exception e) {
						//log.error("fail to load application.properties!", e);
						System.exit(1);
					}
				}
			}
		}

		return instance;
	}

	public static ConfigProperties refresh() {
		if (instance == null) {
			synchronized (ConfigProperties.class) {
				if (instance == null) {
					instance = new ConfigProperties();
				}
			}
		} else {
			instance.clear();
		}

		try {
			InputStream is = instance.getClass().getClassLoader().getResourceAsStream("config.properties");
			instance.load(is);
		} catch (Exception e) {
			//log.error("fail to load config.properties!", e);
			System.exit(1);
		}

		return instance;
	}

}
