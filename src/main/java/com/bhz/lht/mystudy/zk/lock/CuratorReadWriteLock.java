package com.bhz.lht.mystudy.zk.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * a distributed lock implement, used Apache Curator.
 * 
 * @author liuhongtian
 *
 */
public class CuratorReadWriteLock implements ReadWriteLock {

	private CuratorReentrantLock readLock;
	private CuratorReentrantLock writeLock;

	public CuratorReadWriteLock(String zookeeperConnectionString, String lockPath) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
		client.start();
		InterProcessReadWriteLock lock = new InterProcessReadWriteLock(client, lockPath);
		this.readLock = new CuratorReentrantLock(lock.readLock());
		this.writeLock = new CuratorReentrantLock(lock.writeLock());
	}

	@Override
	public Lock readLock() {
		return this.readLock;
	}

	@Override
	public Lock writeLock() {
		return this.writeLock;
	}

}
