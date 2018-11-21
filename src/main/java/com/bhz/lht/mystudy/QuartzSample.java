package com.bhz.lht.mystudy;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.JobBuilder;
import org.quartz.TriggerBuilder;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;

import static org.quartz.CronScheduleBuilder.*;

/**
 * Quartz框架样例代码
 * 
 * @author liuhongtian
 *
 */
public class QuartzSample {

	public static void main(String[] args) throws InterruptedException {

		try {
			// Grab the Scheduler instance from the Factory
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			// and start it off
			scheduler.start();

			// define the job and tie it to our HelloJob class
			JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job1", "group1").build();

			// trigger the job to run, use cron string
			CronTrigger ctrigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
					.withSchedule(cronSchedule("*/5 * * * * ?")).build();

			// tell quartz to schedule the job using our trigger
			scheduler.scheduleJob(job, ctrigger);

			// start a thread to tell the main thread shutdown the scheduler after 30
			// seconds
			new Thread(() -> {
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (QuartzSample.class) {
					QuartzSample.class.notify();
				}
			}).start();

			// main thread wait to shutdown the scheduler
			synchronized (QuartzSample.class) {
				QuartzSample.class.wait();
				scheduler.shutdown();
			}

		} catch (SchedulerException se) {
			se.printStackTrace();
		}
	}
}
