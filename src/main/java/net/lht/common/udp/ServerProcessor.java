package net.lht.common.udp;

/**
 * UDP服务端业务处理逻辑
 * 
 * @author liuhongtian
 *
 */
public interface ServerProcessor {

	/**
	 * 有应答消息的服务端处理
	 * 
	 * @param data 接收到的消息
	 * @return 应答消息
	 */
	public default byte[] process(byte[] data) {
		String receStr = new String(data);
		System.out.println("Server Rece:" + receStr);

		return null;
	}

	/**
	 * 无应答消息的服务端处理
	 * 
	 * @param data 接收到的消息
	 */
	public default void processNoAck(byte[] data) {

	}
}
