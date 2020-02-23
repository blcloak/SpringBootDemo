package com.onekeyweb.dao;

import java.util.HashMap;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class JERecord {
	
	@PrimaryKey
	private String recordID;
	
	@SecondaryKey(relate=Relationship.MANY_TO_ONE)
	private String fromID;
	
	private HashMap<String, String> data;

	public String getRecordID() {
		return recordID;
	}

	public HashMap<String, String> getData() {
		return data;
	}

	public void setData(HashMap<String, String> data) {
		this.data = data;
	}

	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}

	public String getFromID() {
		return fromID;
	}

	public void setFromID(String fromID) {
		this.fromID = fromID;
	}

	
	
}
