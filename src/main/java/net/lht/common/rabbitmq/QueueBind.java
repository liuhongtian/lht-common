package net.lht.common.rabbitmq;

/**
 * 队列绑定
 * 
 * @author liuhongtian
 *
 */
public class QueueBind {

	private String queueName;
	private String exchangeName;
	private String routingKey;

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

}
