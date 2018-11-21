package com.bhz.lht.mystudy;

public class TestPO {
	private String name;
	private int id;
	private int age;
	private int s;

	public TestPO(int id, String name, int age, int s) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.s = s;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}

}
