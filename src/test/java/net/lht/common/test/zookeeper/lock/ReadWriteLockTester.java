package net.lht.common.test.zookeeper.lock;

import java.util.concurrent.locks.ReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.lht.common.zookeeper.lock.CuratorReadWriteLock;

/**
 * 读写锁测试（这里只用到了写锁）
 * 
 * @author liuhongtian
 *
 */
public class ReadWriteLockTester implements Runnable {

	private static final String ZK_STRING = "10.1.1.51:2181,10.1.1.52:2181,10.1.1.53:2181";
	private static final String ZK_LOCK_PATH = "/LLOOCCKK";

	private static OrderCodeGenerator ong = new OrderCodeGenerator(ZK_STRING);

	private Logger logger = LoggerFactory.getLogger(ReadWriteLockTester.class);

//  private static Lock lock = new ReentrantLock();  // 加锁方式（单进程，多线程）
	private ReadWriteLock lock = new CuratorReadWriteLock(ZK_STRING, ZK_LOCK_PATH); // 加锁方式2（多进程，多线程，分布式锁，使用Curator访问Zookeeper实现）
	
	private int tid;
	
	public ReadWriteLockTester(int tid) {
		this.tid = tid;
	}

	@Override
	public void run() {
		String orderCode = null;
		lock.writeLock().lock();
		try {
			// ……业务代码：获取订单编号
			orderCode = ong.getOrderCode();
			logger.info(tid + ": " + Thread.currentThread().getName() + " got lock: =======================>" + orderCode);
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

}
