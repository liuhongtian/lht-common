package net.lht.common.test.zk.lock;

import java.util.Arrays;
import java.util.concurrent.locks.ReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.lht.common.zk.lock.CuratorReadWriteLock;

/**
 * 读写锁测试（这里只用到了写锁）
 * 
 * @author liuhongtian
 *
 */
public class OrderWriteMain implements Runnable {

	private static final String ZK_STRING = "10.1.1.132:2181,10.1.1.133:2181,10.1.1.136:2181";
	private static final String ZK_LOCK_PATH = "/LLOOCCKK";

	private static OrderCodeGenerator ong = new OrderCodeGenerator(ZK_STRING);

	private Logger logger = LoggerFactory.getLogger(OrderWriteMain.class);

//  private static Lock lock = new ReentrantLock();  // 加锁方式（单进程，多线程）
	private ReadWriteLock lock = new CuratorReadWriteLock(ZK_STRING, ZK_LOCK_PATH); // 加锁方式2（多进程，多线程，分布式锁，使用Curator访问Zookeeper实现）

	// 创建订单
	private void createOrder() {
		String orderCode = null;
		lock.writeLock().lock();
		try {
			// ……业务代码：获取订单编号
			orderCode = ong.getOrderCode();
			logger.info(Thread.currentThread().getName() + " got lock: =======================>" + orderCode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.writeLock().unlock();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		// 创建订单
		createOrder();
	}

	public static void main(String[] args) throws InterruptedException {
		int nThread = 100;
		Thread[] ts = new Thread[nThread];
		for(int i = 0; i < nThread; i++) {
			ts[i] = new Thread(new OrderWriteMain());
		}

		Arrays.asList(ts).parallelStream().forEach(t -> {
			t.start();
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

}
