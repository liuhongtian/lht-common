package net.lht.common.rabbitmq;

import java.util.List;

public class Config {

	private int poolSize;
	private String userName;
	private String password;
	private String virtualHost;
	private int portNumber;
	private String hostName;
	private List<String> exchanges;
	private List<String> queues;
	private List<QueueBind> binds;

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public List<String> getExchanges() {
		return exchanges;
	}

	public void setExchanges(List<String> exchanges) {
		this.exchanges = exchanges;
	}

	public List<String> getQueues() {
		return queues;
	}

	public void setQueues(List<String> queues) {
		this.queues = queues;
	}

	public List<QueueBind> getBinds() {
		return binds;
	}

	public void setBinds(List<QueueBind> binds) {
		this.binds = binds;
	}

}
