package net.lht.common.config;

import java.io.InputStream;
import java.util.Properties;

//import org.apache.log4j.Logger;

/**
 * [file].properties
 * 
 * @author liuhongtian
 * 
 */
public class CustomProperties extends Properties {

	private static final long serialVersionUID = -2751730367037476547L;

	//private static final Logger log = Logger.getLogger(CustomProperties.class);

	private String file;

	/**
	 * 使用类路径中的properties文件初始化
	 * 
	 * @param file
	 */
	public CustomProperties(String file) {
		super();
		this.file = file;
		load();
	}

	private void load() {
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(file);
			load(is);
		} catch (Exception e) {
			//log.error("fail to load " + file, e);
			System.exit(1);
		}
	}

	public void refresh() {
		clear();
		load();
	}

}
