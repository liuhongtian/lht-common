package net.lht.common.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.alibaba.fastjson.JSON;

import net.lht.common.file.LocalFileUtils;

// Class --------------------------------------------------------------------------------------------------------
public class MemcachedConfig {
	// Constant(s)
	// ----------------------------------------------------------------------------------------------
	private static final Charset UTF8 = Charset.forName("UTF-8");

	// Variable(s)
	// ----------------------------------------------------------------------------------------------
	private List<String> urls;

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

	private int expireTime;

	public int getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	private int getTimeoutTime;

	public int getGetTimeoutTime() {
		return getTimeoutTime;
	}

	public void setGetTimeoutTime(int getTimeoutTime) {
		this.getTimeoutTime = getTimeoutTime;
	}

	private int setTimeoutTime;

	public int getSetTimeoutTime() {
		return setTimeoutTime;
	}

	public void setSetTimeoutTime(int setTimeoutTime) {
		this.setTimeoutTime = setTimeoutTime;
	}

	// Method(s)
	// ------------------------------------------------------------------------------------------------
	public static MemcachedConfig load(String path) {
		if (path == null) {
			return null;
		}

		String json = null;
		File file = new File(path);
		if (!file.canRead()) {
			file = null;
			return null;
		} else {
			json = LocalFileUtils.readToString(path);
			file = null;
		}

		MemcachedConfig config = JSON.parseObject(json, MemcachedConfig.class);
		file = null;

		return config;
	}

	public static MemcachedConfig loadFromClasspath(String classPath) {
		if (classPath == null) {
			return null;
		}

		String json = LocalFileUtils.readToStringFromClasspath(classPath);
		MemcachedConfig config = JSON.parseObject(json, MemcachedConfig.class);

		return config;
	}

	public void save(String path) {
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(path);

			fos.write(JSON.toJSONString(this).getBytes(UTF8));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					fos = null;
				}
			}
		}
	}
}
