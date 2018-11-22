package net.lht.common.config;

/**
 * use application.properties
 * 
 * @author liuhongtian
 * 
 */
public class ApplicationProperties extends CustomProperties {

	private static final long serialVersionUID = 7159324916694657645L;

	private static CustomProperties instance = new CustomProperties("application.properties");

	public static void reload() {
		ApplicationProperties.instance().reload("application.properties");
	}

	public static CustomProperties instance() {
		return instance;
	}

	public static void main(String[] args) {
		ApplicationProperties.instance().forEach((k, v) -> System.out.println("key=" + k + "; value=" + v));
	}

}
