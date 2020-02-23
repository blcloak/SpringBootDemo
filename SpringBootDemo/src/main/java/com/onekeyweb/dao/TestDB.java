package com.onekeyweb.dao;

public class TestDB {

	public static void main(String[] args) {
		JEBaseAPI oJe = new JEBaseAPI();
		oJe.openDatabase();
		oJe.writeToDatabase("1", "222", false);
		System.out.println(oJe.readFromDatabase("1"));
	}

}
