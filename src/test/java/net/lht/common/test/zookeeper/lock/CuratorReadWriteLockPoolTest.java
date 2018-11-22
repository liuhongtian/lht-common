package net.lht.common.test.zookeeper.lock;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorReadWriteLockPoolTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	//@Test
	public void test() {
		int nThreads = 100;
		List<Future<?>> futures = new ArrayList<>(nThreads);
		ExecutorService pool = Executors.newFixedThreadPool(nThreads);
		Stream<Integer> stream = Stream.iterate(0, n -> n + 1).limit(nThreads);
		stream.parallel().forEach(n -> futures.add(pool.submit(new ReadWriteLockTester(n))));
		futures.stream().parallel().forEach(f -> {
			try {
				f.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
		pool.shutdown();
		assertTrue(true);
	}

}
