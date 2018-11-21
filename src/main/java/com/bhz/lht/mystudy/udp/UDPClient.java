package com.bhz.lht.mystudy.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {

	private static String sendStr = "SendString";
	private static String netAddress = "10.1.1.101";
	//private static String netAddress = "10.1.1.222";
	private static final int PORT_NUM = 30702;
	//private static final int PORT_NUM = 5002;

	private static DatagramSocket datagramSocket;
	private static DatagramPacket datagramPacket;

	public static void main(String... args) {
		try {

			/*** 发送数据 ***/
			// 初始化datagramSocket,注意与前面Server端实现的差别
			datagramSocket = new DatagramSocket();
			// 使用DatagramPacket(byte buf[], int length, InetAddress address, int
			// port)函数组装发送UDP数据报
			byte[] buf = sendStr.getBytes();
			InetAddress address = InetAddress.getByName(netAddress);
			datagramPacket = new DatagramPacket(buf, buf.length, address, PORT_NUM);
			// 发送数据
			datagramSocket.send(datagramPacket);

			/*** 接收数据 ***/
			byte[] receBuf = new byte[1024];
			DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
			datagramSocket.receive(recePacket);

			String receStr = new String(recePacket.getData(), 0, recePacket.getLength());
			System.out.println("Client Rece Ack:" + receStr);
			System.out.println(recePacket.getPort());

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭socket
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}
	}
}

