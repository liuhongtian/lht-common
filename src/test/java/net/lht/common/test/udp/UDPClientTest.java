package net.lht.common.test.udp;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
//import org.junit.Test;

import net.lht.common.udp.UDPClient;

public class UDPClientTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	// @Test
	public void test() {
		int poolSize = 100;
		ExecutorService pool = Executors.newFixedThreadPool(poolSize);
		Random r = new Random();
		for (int i = 0; i < poolSize; i++) {
			System.out.println("submit");
			pool.submit(() -> {
				for (int j = 0; j < 500; j++) {
					try {
						Thread.sleep(Math.abs(r.nextInt() % 2000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					byte[] ack = UDPClient.sendAndReceive("127.0.0.1", 5002, "SendStringliuhongtian".getBytes());
					System.out.println(j + " Server ACK: " + new String(ack));
				}
			});
		}
		pool.shutdown();
		assertTrue(true);
	}

}
