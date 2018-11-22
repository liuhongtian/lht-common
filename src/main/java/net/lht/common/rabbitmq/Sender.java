package net.lht.common.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import net.lht.common.misc.LocalFileUtils;

/**
 * RabbitMQ消息发送工具
 * 
 * @author liuhongtian
 *
 */
public class Sender {

	private Connection connection;
	private Channel channel;
	private Config config;

	/**
	 * 根据配置初始化RabbitMQ连接
	 * 
	 * @param file
	 *            配置文件名称，需放在classpath根目录下。
	 */
	public Sender(String file) {
		init(file);
	}

	/**
	 * 初始化RabbitMQ连接
	 * 
	 * @param config
	 * @return
	 */
	public boolean init(String file) {
		this.config = JSON.parseObject(LocalFileUtils.readToStringFromClasspath(file), Config.class);
		System.out.println(config.getHostName());

		boolean result = false;

		if (this.channel == null || !this.channel.isOpen()) {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUsername(config.getUserName());
			factory.setPassword(config.getPassword());
			factory.setVirtualHost(config.getVirtualHost());
			factory.setHost(config.getHostName());
			factory.setPort(config.getPortNumber());
			try {
				this.connection = factory.newConnection();
				this.channel = this.connection.createChannel();
				result = true;

			} catch (IOException | TimeoutException e) {
				e.printStackTrace();
			}

		} else {
			result = true; // 通道已建立并已打开则直接返回true
		}

		return result;
	}

	/**
	 * 发送消息
	 * 
	 * @param msg
	 *            消息
	 * @param exchangeName
	 *            交换器
	 * @param routingKey
	 *            路由键
	 * @return
	 */
	public boolean send(byte[] msg, String exchangeName, String routingKey) {
		boolean result = false;

		try {
			channel.basicPublish(exchangeName, routingKey, new AMQP.BasicProperties.Builder().build(), msg);
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 关闭通道及连接
	 * 
	 */
	public void close() {
		if (channel != null && channel.isOpen()) {
			try {
				channel.close();
			} catch (IOException | TimeoutException e) {
				e.printStackTrace();
			}
		}
		if (connection != null && connection.isOpen()) {
			try {
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
