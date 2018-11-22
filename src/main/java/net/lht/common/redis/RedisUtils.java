package net.lht.common.redis;

//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.UUID;
import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.alibaba.fastjson.JSON;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisFuture;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.async.RedisAsyncCommands;

import net.lht.common.file.LocalFileUtils;

/**
 * Redis工具
 * 
 * @author liuhongtian
 *
 */
public class RedisUtils {

	public static String DT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

	private int EXPIRE_SECONDS;
	private int TIMEOUT_SECONDS;

	private StatefulRedisConnection<String, String> conn;

	public RedisUtils(String configFile) {
		RedisConfig config = JSON.parseObject(LocalFileUtils.readToStringFromClasspath(configFile), RedisConfig.class);
		EXPIRE_SECONDS = config.getExpireSeconds();
		TIMEOUT_SECONDS = config.getTimeoutSeconds();
		conn = RedisClient.create(config.getUrl()).connect();
	}

	/**
	 * 关闭Redis连接
	 */
	public void shutdown() {
		conn.close();
	}

	/**
	 * 设置缓存值
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            缓存字符串
	 * @return true-成功；false-失败
	 */
	public boolean set(String key, String value) {
		boolean result = false;
		RedisAsyncCommands<String, String> commands = conn.async();
		RedisFuture<String> res = commands.set(key, value);
		if (EXPIRE_SECONDS > 0) {
			commands.expire(key, EXPIRE_SECONDS);
		}
		try {
			if (res.get(TIMEOUT_SECONDS, TimeUnit.SECONDS) != null) {
				result = true;
			}
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 将field&value合并到缓存中的指定map
	 * 
	 * @param key
	 *            缓存中的map名称
	 * @param field
	 *            map中的key
	 * @param value
	 *            map中的value
	 * @return
	 */
	public boolean hset(String key, String field, String value) {
		boolean result = false;
		RedisAsyncCommands<String, String> commands = conn.async();
		RedisFuture<Boolean> res = commands.hset(key, field, value);
		if (EXPIRE_SECONDS > 0) {
			commands.expire(key, EXPIRE_SECONDS);
		}
		try {
			if (res.get(TIMEOUT_SECONDS, TimeUnit.SECONDS) != null) {
				result = true;
			}
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 将map合并到缓存中的指定map
	 * 
	 * @param key
	 *            缓存中的map名称
	 * @param map
	 *            待合并的map
	 * @return
	 */
	public boolean hmset(String key, Map<String, String> map) {
		boolean result = false;
		RedisAsyncCommands<String, String> commands = conn.async();
		RedisFuture<String> res = commands.hmset(key, map);
		if (EXPIRE_SECONDS > 0) {
			commands.expire(key, EXPIRE_SECONDS);
		}
		try {
			if (res.get(TIMEOUT_SECONDS, TimeUnit.SECONDS) != null) {
				result = true;
			}
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取缓存值
	 * 
	 * @param key
	 *            key
	 * @return value，无缓存或出现任何异常时，返回null
	 */
	public String get(String key) {
		RedisAsyncCommands<String, String> commands = conn.async();
		RedisFuture<String> res = commands.get(key);
		String value = null;
		if (TIMEOUT_SECONDS > 0) {
			try {
				value = res.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				e.printStackTrace();
			}
		} else {
			try {
				value = res.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	/**
	 * 获取缓存中指定map的指定值
	 * 
	 * @param key
	 *            缓存中的map名称
	 * @param field
	 *            缓存中map的key
	 * @return
	 */
	public String hget(String key, String field) {
		RedisAsyncCommands<String, String> commands = conn.async();
		RedisFuture<String> res = commands.hget(key, field);
		String value = null;
		if (TIMEOUT_SECONDS > 0) {
			try {
				value = res.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				e.printStackTrace();
			}
		} else {
			try {
				value = res.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	/**
	 * 获取缓存中指定map的一系列值
	 * 
	 * @param key
	 *            缓存中的map的名称
	 * @param fields
	 *            待获取的map中的一系列key
	 * @return
	 */
	public List<String> hmget(String key, String... fields) {
		RedisAsyncCommands<String, String> commands = conn.async();
		RedisFuture<List<String>> res = commands.hmget(key, fields);
		List<String> value = null;
		if (TIMEOUT_SECONDS > 0) {
			try {
				value = res.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				e.printStackTrace();
			}
		} else {
			try {
				value = res.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	/**
	 * 设置缓存值
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            缓存实体
	 * @return true-成功；false-失败
	 */
	public boolean setObject(String key, Object value) {
		String json = JSON.toJSONString(value);
		return set(key, json);
	}

	/**
	 * 将field&value合并到缓存中的指定map
	 * 
	 * @param key
	 *            缓存中的map名称
	 * @param field
	 *            map中的key
	 * @param value
	 *            map中的value
	 * @return
	 */
	public boolean hsetObject(String key, String field, Object value) {
		String json = JSON.toJSONString(value);
		return hset(key, field, json);
	}

	/**
	 * 将map合并到缓存中的指定map
	 * 
	 * @param key
	 *            缓存中的map名称
	 * @param map
	 *            待合并的map
	 * @return
	 */
	public boolean hmsetObject(String key, Map<String, Object> map) {
		Map<String, String> jsonMap = new HashMap<>();
		map.keySet().parallelStream().forEach(n -> {
			jsonMap.put(n, JSON.toJSONString(map.get(n)));
		});
		return hmset(key, jsonMap);
	}

	/**
	 * 获取缓存值
	 * 
	 * @param key
	 *            key
	 * @param clazz
	 *            缓存实体类型
	 * @return value，无缓存或出现任何异常时，返回null
	 */
	public <T> T getObject(String key, Class<T> clazz) {
		T value = null;
		String json = get(key);
		if (json != null) {
			value = JSON.parseObject(json, clazz);
		}
		return value;
	}

	/**
	 * 获取缓存值
	 * 
	 * @param key
	 *            缓存中的map名称
	 * @param clazz
	 *            缓存map中的实体类型
	 * @param field
	 *            map中的key
	 * @return value，无缓存或出现任何异常时，返回null
	 */
	public <T> T hgetObject(String key, Class<T> clazz, String field) {
		T value = null;
		String json = hget(key, field);
		if (json != null) {
			value = JSON.parseObject(json, clazz);
		}
		return value;
	}

	/**
	 * 获取缓存值
	 * 
	 * @param key
	 *            缓存中的map名称
	 * @param clazz
	 *            缓存map中的实体类型
	 * @param fields
	 *            map中的key
	 * @return value，无缓存或出现任何异常时，返回空列表
	 */
	public <T> List<T> hmgetObject(String key, Class<T> clazz, String... fields) {
		List<T> value = new ArrayList<>();
		List<String> jsons = hmget(key, fields);
		if (jsons != null) {
			jsons.parallelStream().forEachOrdered(n -> {
				value.add(JSON.parseObject(n, clazz));
			});
		}
		return value;
	}

	public static void main(String[] args) {

		// ExecutorService tpool = Executors.newFixedThreadPool(1000);
		// for (int i = 0; i < 1000; i++) {
		// tpool.submit(new Runnable() {
		// @Override
		// public void run() {
		// while (true) {
		// String key = UUID.randomUUID().toString();
		// LocalDateTime begin = LocalDateTime.now();
		// System.out.println("begin:" +
		// begin.format(DateTimeFormatter.ofPattern(DT_PATTERN)));
		// set(key, key);
		// String value = get(key);
		// System.out.println(value);
		// LocalDateTime end = LocalDateTime.now();
		// System.out.println("end :" +
		// end.format(DateTimeFormatter.ofPattern(DT_PATTERN)));
		// System.out.println(end.getNano());
		// }
		//
		// }
		// });
		// }

		System.out.println(new RedisUtils("redis.json").hget("mymap", "myname"));

	}

}
