package com.onekeyweb.dao;

import java.util.List;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class JEIndex {
	
	@PrimaryKey
	private String colID;
	
	private List<List<String>> index;

	public String getColID() {
		return colID;
	}

	public void setColID(String colID) {
		this.colID = colID;
	}

	public List<List<String>> getIndex() {
		return index;
	}

	public void setIndex(List<List<String>> index) {
		this.index = index;
	}
	
	
}
