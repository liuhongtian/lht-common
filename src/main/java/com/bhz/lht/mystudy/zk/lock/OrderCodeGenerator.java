package com.bhz.lht.mystudy.zk.lock;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class OrderCodeGenerator {

	private static final String ORDER_CODE_SN_PATH = "/OrderCodeSn";

	private CuratorFramework client;

	public OrderCodeGenerator(String zookeeperConnectionString) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, retryPolicy);
		client.start();
	}

	// 按照“年-月-日-小时-分钟-秒-自增长序列”的规则生成订单编号
	public String getOrderCode() throws Exception {
		byte[] data = null;

		// first
		if (client.checkExists().forPath(ORDER_CODE_SN_PATH) == null) {
			client.create().forPath(ORDER_CODE_SN_PATH, new Integer(0).toString().getBytes());
		}

		data = client.getData().forPath(ORDER_CODE_SN_PATH);

		int i = Integer.parseInt(new String(data)) + 1;
		client.setData().forPath(ORDER_CODE_SN_PATH, new Integer(i).toString().getBytes());

		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-");
		String orderCode = sdf.format(now) + i;
		// System.out.println(orderCode);
		return orderCode;
	}

}
