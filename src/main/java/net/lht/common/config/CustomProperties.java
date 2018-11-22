package net.lht.common.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * [file].properties
 * 
 * @author liuhongtian
 * 
 */
public class CustomProperties extends Properties {

	private static final long serialVersionUID = -2751730367037476547L;

	/**
	 * 使用类路径中的application.properties文件初始化
	 * 
	 * ]
	 */
	public CustomProperties() {
		this("application.properties");
	}

	/**
	 * 使用类路径中的properties文件初始化
	 * 
	 * @param file
	 */
	public CustomProperties(String file) {
		super();
		load(file);
	}

	private void load(String file) {
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(file);
			load(is);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void reload(String file) {
		clear();
		load(file);
	}

	public static void main(String[] args) {
		CustomProperties instance = new CustomProperties("application.properties");
		instance.forEach((k, v) -> System.out.println("key=" + k + "; value=" + v));
	}

}
