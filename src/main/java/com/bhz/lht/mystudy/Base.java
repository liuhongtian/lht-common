package com.bhz.lht.mystudy;

public class Base {

	private String name = "base";

	public Base() {
		tellName();
		printName();
	}

	private void tellName() {
		System.out.println(name);
	}

	private void printName() {
		System.out.println(name);
	}

	public static void main(String[] args) {
		new Base();
	}

}
