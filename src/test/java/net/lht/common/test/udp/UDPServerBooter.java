package net.lht.common.test.udp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import net.lht.common.udp.ServerProcessor;
import net.lht.common.udp.UDPServer;

public class UDPServerBooter {

	public static void main(String[] args) {

		int port = 5002;

		ServerProcessor processor = new ServerProcessor() {

			private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

			@Override
			public byte[] process(byte[] data) {
				LocalDateTime time = LocalDateTime.now();
				String receStr = new String(data);
				String ack = time.format(formatter) + " - Server Reced:" + receStr;
				System.out.println(ack);
				return ack.getBytes();
			}

		};

		UDPServer server = new UDPServer(processor, port);
		server.start();

	}

}
