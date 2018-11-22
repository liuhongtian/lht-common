package net.lht.common.rabbitmq;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

import net.lht.common.misc.LocalFileUtils;

/**
 * 长连接消费者，用于即时接收消息
 * 
 * @author liuhongtian
 *
 */
public class Consumer {

	private Connection connection;
	private Channel channel;
	private Config config;
	private String queueName;

	/**
	 * 根据配置初始化RabbitMQ连接
	 * 
	 * @param file
	 *            配置文件名称，需放在classpath根目录下。
	 * @param queueName
	 *            本Consumer需要消费的队列名
	 * @param handler
	 *            消费消息时的业务逻辑类
	 * @param onlyBody
	 *            业务逻辑是否只处理消息体（如果是，将不能定制化处理消息头的信息。）
	 */
	public Consumer(String file, String queueName, Handler handler, boolean onlyBody) {
		this.queueName = queueName;
		this.config = JSON.parseObject(LocalFileUtils.readToStringFromClasspath(file), Config.class);
		init(file, handler, onlyBody);
	}

	/**
	 * @param file
	 *            配置文件名称，需放在classpath根目录下。
	 * @param handler
	 *            消费消息时的业务逻辑类
	 * @param onlyBody
	 *            业务逻辑是否只处理消息体（如果是，将不能定制化处理消息头的信息。
	 * @return
	 */
	public boolean init(String file, Handler handler, boolean onlyBody) {

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

				boolean autoAck = false;
				this.channel.basicConsume(this.queueName, autoAck, "", new DefaultConsumer(channel) {
					@Override
					public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
							byte[] body) throws IOException {
						long deliveryTag = envelope.getDeliveryTag();
						boolean success = false;
						if(onlyBody) {
							success = handler.handleBody(body);
						} else {
							success = handler.handleDelivery(envelope, properties, body);
						}
						if (success) {
							channel.basicAck(deliveryTag, false);
						}
					}
				});

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
	 * 处理消息体的样例类，仅打印消息体字符串到控制台
	 * 
	 * @author liuhongtian
	 *
	 */
	private static class SampleHandler extends BaseHandler {

		@Override
		public boolean handleBody(byte[] body) {
			try {
				System.out.println(new String(body, "UTF-8"));
				return true;
			} catch (UnsupportedEncodingException e) {
				return false;
			}
		}

		@Override
		public boolean handleDelivery(Envelope envelope, BasicProperties properties, byte[] body) {
			try {
				System.out.println(envelope.getExchange() + "," + envelope.getRoutingKey() + ","
						+ properties.getMessageId() + ":" + new String(body, "UTF-8"));
				return true;
			} catch (UnsupportedEncodingException e) {
				return false;
			}
		}

	}

	public static void main(String[] args) throws InterruptedException {

		System.out.println("now consumerA:");
		Consumer consumerA = new Consumer("rabbit.json", "a", new Consumer.SampleHandler(), false);
		Thread.sleep(3000);
		consumerA.close();

		System.out.println("now consumerB:");
		Consumer consumerB = new Consumer("rabbit.json", "b", new Consumer.SampleHandler(), true);
		Thread.sleep(3000);
		consumerB.close();

		System.out.println("now consumerC:");
		Consumer consumerC = new Consumer("rabbit.json", "c", new Consumer.SampleHandler(), true);
		Thread.sleep(3000);
		consumerC.close();

	}

}
