package net.lht.common.zookeeper.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * a distributed lock implement, used Apache Curator.
 * 
 * @author liuhongtian
 *
 */
public class CuratorReentrantLock implements Lock {

	private InterProcessMutex lock;

	/**
	 * Used as a ReentrantLock
	 * 
	 * @param zookeeperConnectionString
	 * @param lockPath
	 */
	public CuratorReentrantLock(String zookeeperConnectionString, String lockPath) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
		client.start();
		this.lock = new InterProcessMutex(client, lockPath);
	}

	/**
	 * Used by CuratorReadWriteLock, holding the actual read and write lock.<br>
	 * WARN: the instance of CuratorFramework is create by CuratorReadWriteLock
	 * instance.
	 * 
	 * @param lock
	 */
	protected CuratorReentrantLock(InterProcessMutex lock) {
		this.lock = lock;
	}

	@Override
	public void lock() {
		try {
			this.lock.acquire();
		} catch (Exception e) {
			lock();
			e.printStackTrace();
		}
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO NOUSE
	}

	@Override
	public boolean tryLock() {
		try {
			// 缺省等待5秒。
			return this.lock.acquire(5000, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		try {
			return this.lock.acquire(time, unit);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void unlock() {
		if (this.lock.isOwnedByCurrentThread()) {
			try {
				this.lock.release();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Condition newCondition() {
		// TODO NOUSE
		return null;
	}

}
