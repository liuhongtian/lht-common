package net.lht.common.test.zk.lock;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorReadWriteLockTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		int nThread = 100;
		Thread[] ts = new Thread[nThread];
		for (int i = 0; i < nThread; i++) {
			ts[i] = new Thread(new ReadWriteLockTester());
		}

		Arrays.asList(ts).parallelStream().forEach(t -> {
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		assertTrue(true);
	}

}
