package net.lht.common.test.udp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import net.lht.common.udp.ServerProcessor;
import net.lht.common.udp.UDPServerMultiThread;

public class UDPServerBooter {

	public static void main(String[] args) {

		int port = 5002;

		ServerProcessor processor = new ServerProcessor() {

			private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

			@Override
			public byte[] process(byte[] data) {
				LocalDateTime time = LocalDateTime.now();
				String receStr = new String(data);
				String ack = time.format(formatter) + " - Server Receved:" + receStr;
				System.out.println(ack);
				return ack.getBytes();
			}

		};

		UDPServerMultiThread server = new UDPServerMultiThread(processor, port);
		server.start();

	}

}
