package net.lht.common.test.zookeeper.lock;

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
		int nThreads = 100;
		Thread[] ts = new Thread[nThreads];
		for (int i = 0; i < nThreads; i++) {
			ts[i] = new Thread(new ReadWriteLockTester(i));
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
