package net.lht.common.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * 单线程运行的UDP服务器。
 * 
 * @author liuhongtian
 *
 */
public class UDPServerSingleThread {

	private ServerProcessor processor = null; // 业务逻辑处理

	private int port;

	// 用以存放接收数据的字节缓冲区长度
	private int maxDataLength;

	public UDPServerSingleThread(ServerProcessor processor, int port) {
		this(processor, port, 1024);
	}

	public UDPServerSingleThread(ServerProcessor processor, int port, int maxDataLength) {
		this.processor = processor;
		this.port = port;
		this.maxDataLength = maxDataLength;

		if (processor == null) {
			processor = new ServerProcessor() {
			};
		}
	}

	public void start() {
		while (true) {
			try (DatagramSocket datagramSocket = new DatagramSocket(this.port);) {
				DatagramPacket datagramPacket = new DatagramPacket(new byte[this.maxDataLength], this.maxDataLength);
				datagramSocket.receive(datagramPacket);// receive()来等待接收UDP数据报

				byte[] data = datagramPacket.getData();

				/***** 返回ACK消息数据报 */
				// 组装数据报
				byte[] ack = processor.process(data);

				if (ack == null) {
					ack = new byte[0];
				}

				DatagramPacket sendPacket = new DatagramPacket(ack, ack.length, datagramPacket.getAddress(),
						datagramPacket.getPort());
				// 发送消息
				datagramSocket.send(sendPacket);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
