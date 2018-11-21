package net.lht.common.test.udp;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.lht.common.udp.UDPClient;

public class UDPClientTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		byte[] ack = UDPClient.sendAndReceive("127.0.0.1", 5002, "SendStringliuhongtian".getBytes());
		System.out.println("Server ACK: " + new String(ack));
	}

}
