package net.lht.common.rabbitmq;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import net.lht.common.file.LocalFileUtils;

/**
 * 短连接消费者，用于定期接收消息<br>
 * 这里所谓的“短连接”，是指用户需要明确执行connect、disconnect方法，来进行RabbitMQ的连接与断开，send方法不维护连接（如果连接未断开，可以重复调用send方法，以达到批量消费消息的目的）。
 * 
 * @author liuhongtian
 *
 */
public class Receiver {

	private Connection connection;
	private Channel channel;
	private Config config;
	private String queueName;

	private boolean autoAck = false;

	/**
	 * 根据配置初始化RabbitMQ信息
	 * 
	 * @param file
	 *            配置文件名称，需放在classpath根目录下。
	 * @param queueName
	 *            本Receiver需要消费的队列名
	 */
	public Receiver(String file, String queueName) {
		this.queueName = queueName;
		this.config = JSON.parseObject(LocalFileUtils.readToStringFromClasspath(file), Config.class);
	}

	/**
	 * 初始化RabbitMQ连接
	 * 
	 * @return
	 */
	public boolean connect() {

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

	/**
	 * 从消息队列消费一条消息（仅含消息体）并确认接收
	 * 
	 * @return 消息体
	 */
	public byte[] getBodyAndAcknowledge() {
		byte[] body = null;
		try {
			GetResponse msg = this.channel.basicGet(this.queueName, this.autoAck);
			if (msg != null) {
				body = msg.getBody();
				this.channel.basicAck(msg.getEnvelope().getDeliveryTag(), false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}

	/**
	 * 从消息队列消费一条消息（完整消息，包含消息头）并确认接收
	 * 
	 * @return 消息
	 */
	public GetResponse getMessageAndAcknowledge() {
		GetResponse msg = null;
		try {
			msg = this.channel.basicGet(this.queueName, this.autoAck);
			if (msg != null) {
				this.channel.basicAck(msg.getEnvelope().getDeliveryTag(), false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 从消息队列消费一条消息（完整消息，包含消息头）<br>
	 * 请注意此方法需要自行调用acknowledge方法进行接收确认。
	 * 
	 * @return 消息
	 */
	public GetResponse getMessage() {
		GetResponse msg = null;
		try {
			msg = this.channel.basicGet(this.queueName, this.autoAck);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}

	public boolean acknowledge(GetResponse msg) {
		boolean result = false;

		if (msg != null) {
			try {
				this.channel.basicAck(msg.getEnvelope().getDeliveryTag(), false);
				result = true;
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			result = true;
		}

		return result;
	}

	/**
	 * 处理消息体的样例类，仅打印消息体字符串到控制台
	 * 
	 * @author liuhongtian
	 *
	 */
	private static class SampleHandler extends BaseHandler {

		@Override
		public boolean handleBody(byte[] body) {
			try {
				if (body != null) {
					System.out.println(new String(body, "UTF-8"));
				}
				return true;
			} catch (UnsupportedEncodingException e) {
				return true;
			}
		}

	}

	public static void main(String[] args) throws InterruptedException {

		SampleHandler handler = new Receiver.SampleHandler();

		GetResponse msg = null;

		System.out.println("now receiverA:");
		Receiver receiverA = new Receiver("rabbit.json", "a");
		receiverA.connect();
		msg = receiverA.getMessage();
		if (msg != null) {
			handler.handleBody(msg.getBody());
		}
		receiverA.acknowledge(msg);
		handler.handleBody(receiverA.getBodyAndAcknowledge());
		Thread.sleep(3000);
		receiverA.close();

		System.out.println("now receiverB:");
		Receiver receiverB = new Receiver("rabbit.json", "b");
		receiverB.connect();
		handler.handleBody(receiverB.getBodyAndAcknowledge());
		handler.handleBody(receiverB.getBodyAndAcknowledge());
		Thread.sleep(3000);
		receiverB.close();

		System.out.println("now receiverC:");
		Receiver receiverC = new Receiver("rabbit.json", "c");
		receiverC.connect();
		handler.handleBody(receiverC.getBodyAndAcknowledge());
		handler.handleBody(receiverC.getBodyAndAcknowledge());
		Thread.sleep(3000);
		receiverC.close();

	}

}
