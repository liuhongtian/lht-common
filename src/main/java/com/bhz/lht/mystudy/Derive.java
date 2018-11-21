package com.bhz.lht.mystudy;

public class Derive extends Base {

	private String name = "derive";

	public Derive() {
		tellName();
		printName();
	}

	public void tellName() {
		System.out.println(name);
	}

	public void printName() {
		System.out.println(name);
	}

	public static void main(String[] args) {
		new Derive();
	}

}
