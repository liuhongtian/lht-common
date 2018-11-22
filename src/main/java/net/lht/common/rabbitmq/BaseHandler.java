package net.lht.common.rabbitmq;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

public class BaseHandler implements Handler {

	@Override
	public boolean handleBody(byte[] body) {
		return false;
	}

	@Override
	public boolean handleDelivery(Envelope envelope, BasicProperties properties, byte[] body) {
		return false;
	}

}
