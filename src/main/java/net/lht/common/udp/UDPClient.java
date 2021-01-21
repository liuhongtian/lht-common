package net.lht.common.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {

	/**
	 * 不接收响应报文，适用于不关注响应及服务器无响应报文的情况。
	 * 
	 * @param host
	 * @param port
	 * @param data
	 */
	public static void send(String host, int port, byte[] data) {
		try (DatagramSocket datagramSocket = new DatagramSocket();) {

			/*** 发送数据 ***/
			InetAddress address = InetAddress.getByName(host);
			DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, port);
			datagramSocket.send(datagramPacket);

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接收响应报文，适用于关注响应且服务器有响应报文的情况。
	 * 
	 * @param host
	 * @param port
	 * @param data
	 * @return
	 */
	public static byte[] sendAndReceive(String host, int port, byte[] data) {
		try (DatagramSocket datagramSocket = new DatagramSocket();) {

			/*** 发送数据 ***/
			InetAddress address = InetAddress.getByName(host);
			DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, port);
			datagramSocket.send(datagramPacket);

			/*** 接收数据 ***/
			byte[] receBuf = new byte[1024];
			DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
			datagramSocket.receive(recePacket);

			return recePacket.getData();

		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
