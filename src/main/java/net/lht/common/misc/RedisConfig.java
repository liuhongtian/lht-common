package net.lht.common.misc;

public class RedisConfig {

	private int maxIdle;
	private int maxActive;
	private String url;
	private int expireSeconds;
	private int timeoutSeconds;

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getExpireSeconds() {
		return expireSeconds;
	}

	public void setExpireSeconds(int expireSeconds) {
		this.expireSeconds = expireSeconds;
	}

	public int getTimeoutSeconds() {
		return timeoutSeconds;
	}

	public void setTimeoutSeconds(int timeoutSeconds) {
		this.timeoutSeconds = timeoutSeconds;
	}

}
