package com.bhz.lht.mystudy;

import sun.misc.Signal;
import sun.misc.SignalHandler;

@SuppressWarnings("restriction")
public class SignalHandlerExample implements SignalHandler {

	@Override
	public void handle(Signal sig) {
		System.out.println("receive SIGNAL: " + sig.getName());
		System.exit(0);
	}
	
	public static void main(String... args) {
		Signal.handle(new Signal("TERM"), new SignalHandlerExample());
		//Signal.handle(new Signal("KILL"), new SignalHandlerExample());
		while(true) {
			System.out.println("waitting ...");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}