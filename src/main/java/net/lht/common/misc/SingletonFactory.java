package net.lht.common.misc;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例模式单例对象工厂，被单例化的类，必须具有缺省构造方法。<br>
 * 使用了双重检查锁定。
 * 
 * @author liuhongtian
 * 
 */
public final class SingletonFactory {

	private static Map<String, Object> instances = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public static <T> T instance(Class<T> clazz) {
		Object instance = instances.get(clazz.getName());
		if (instance == null) {
			synchronized (clazz) {
				if (instance == null) {
					try {
						instance = (T) clazz.newInstance();
						instances.put(clazz.getName(), instance);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return (T) instance;
	}

}
