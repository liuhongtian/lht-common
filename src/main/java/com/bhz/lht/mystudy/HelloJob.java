package com.bhz.lht.mystudy;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class HelloJob implements Job {

	protected static int runningCount = 0;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("liuhongtian's job running " + runningCount++ + "!");

	}

}
