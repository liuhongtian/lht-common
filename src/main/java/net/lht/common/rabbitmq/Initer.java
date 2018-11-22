package net.lht.common.rabbitmq;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import net.lht.common.file.LocalFileUtils;

/**
 * RabbitMQ队列、交换器、绑定初始化工具
 * 
 * @author liuhongtian
 *
 */
public class Initer {

	/**
	 * 根据配置初始化RabbitMQ的交换器、队列以及绑定
	 * 
	 * @param file
	 *            配置文件名称，需放在classpath根目录下。
	 * @return
	 */
	public static boolean init(String file) {

		Config config = JSON.parseObject(LocalFileUtils.readToStringFromClasspath(file), Config.class);

		boolean result = false;

		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername(config.getUserName());
		factory.setPassword(config.getPassword());
		factory.setVirtualHost(config.getVirtualHost());
		factory.setHost(config.getHostName());
		factory.setPort(config.getPortNumber());

		Channel channel = null;
		try (Connection connection = factory.newConnection();) {

			channel = connection.createChannel();

			for (String exchangeName : config.getExchanges()) {
				System.out.println("init exchange: " + exchangeName);
				channel.exchangeDeclare(exchangeName, "direct", true);
			}
			for (String queueName : config.getQueues()) {
				System.out.println("init queue: " + queueName);
				channel.queueDeclare(queueName, true, false, false, null);
			}
			for (QueueBind bind : config.getBinds()) {
				channel.queueBind(bind.getQueueName(), bind.getExchangeName(), bind.getRoutingKey());
			}
			result = true;

		} catch (IOException | TimeoutException e) {
			e.printStackTrace();

		}

		return result;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {

		Initer.init("rabbit.json");

	}

}
