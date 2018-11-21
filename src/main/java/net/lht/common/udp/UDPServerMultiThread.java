package net.lht.common.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPServerMultiThread {

	private class Worker implements Runnable {
		private byte[] data;
		private InetAddress address;
		private int port;

		public Worker(byte[] data, InetAddress address, int port) {
			this.data = data;
			this.address = address;
			this.port = port;
		}

		@Override
		public void run() {
			/***** 返回ACK消息数据报 */
			// 组装数据报
			byte[] ack = processor.process(data);

			if (ack == null) {
				ack = new byte[0];
			}

			DatagramPacket sendPacket = new DatagramPacket(ack, ack.length, address, port);
			// 发送消息
			try {
				datagramSocket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private ServerProcessor processor = null; // 业务逻辑处理
	private DatagramSocket datagramSocket = null;

	private int port;

	// 用以存放接收数据的字节缓冲区长度
	private int maxDataLength;

	public UDPServerMultiThread(ServerProcessor processor, int port) {
		this(processor, port, 1024);
	}

	public UDPServerMultiThread(ServerProcessor processor, int port, int maxDataLength) {
		this.processor = processor;
		this.port = port;
		this.maxDataLength = maxDataLength;

		if (processor == null) {
			processor = new ServerProcessor() {
			};
		}
	}

	public void start() {
		// 初始化线程池
		ExecutorService pool = Executors.newFixedThreadPool(8);

		// 初始化Socket
		try {
			datagramSocket = new DatagramSocket(this.port);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1); // Server failed to startup.
		}

		// 消息循环
		while (true) {
			try {
				DatagramPacket datagramPacket = new DatagramPacket(new byte[this.maxDataLength], this.maxDataLength);
				datagramSocket.receive(datagramPacket);// 等待接收UDP数据报
				pool.submit(
						new Worker(datagramPacket.getData(), datagramPacket.getAddress(), datagramPacket.getPort()));// 提交线程池处理消息
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
