package net.lht.common.rabbitmq;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

/**
 * 消息处理接口，消费者应实现此接口，以消费消息。
 * 
 * @author liuhongtian
 *
 */
public interface Handler {

	/**
	 * 处理消息体的方法
	 * 
	 * @param body
	 * @return
	 */
	public boolean handleBody(byte[] body);

	/**
	 * 处理整个消息的方法
	 * 
	 * @param envelope
	 * @param properties
	 * @param body
	 * @return
	 */
	public boolean handleDelivery(Envelope envelope, BasicProperties properties, byte[] body);

}
