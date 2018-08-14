package com.onekeyweb.dao;

public class TestDB {

	public static void main(String[] args) {
		JE oJe = new JE();
		oJe.openDatabase();
		oJe.writeToDatabase("1", "222", false);
		System.out.println(oJe.readFromDatabase("1"));
	}

}
