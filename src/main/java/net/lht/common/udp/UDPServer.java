package net.lht.common.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer {

	private ServerProcessor processor = null; // 业务逻辑处理

	private int port;

	// 用以存放接收数据的字节数组
	private byte[] receMsgs;

	public UDPServer(ServerProcessor processor, int port) {
		this(processor, port, 1024);
	}

	public UDPServer(ServerProcessor processor, int port, int maxDataLength) {
		this.processor = processor;
		this.port = port;
		this.receMsgs = new byte[maxDataLength];
	}

	public void start() {
		while (true) {
			try (DatagramSocket datagramSocket = new DatagramSocket(this.port);) {

				DatagramPacket datagramPacket = new DatagramPacket(receMsgs, receMsgs.length);
				datagramSocket.receive(datagramPacket);// receive()来等待接收UDP数据报

				byte[] data = datagramPacket.getData();

				if (processor == null) {
					processor = new ServerProcessor() {
					};
				}

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

			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * these codes just for test!
	 * 
	 * @param args
	 */
	public static void main(String... args) {
		UDPServer server = null;
		if (args.length < 1) {
			System.out.println("Need parameter(s):\n\t[port](peremptory)\t[maxDataLength](optional)");
			System.exit(0);
		} else if (args.length < 2) {
			server = new UDPServer(null, Integer.parseInt(args[0]));
		} else {
			server = new UDPServer(null, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		}

		if (server != null) {
			server.start();
		}
	}
}
