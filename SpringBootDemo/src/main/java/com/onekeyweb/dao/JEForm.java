package com.onekeyweb.dao;

import java.util.HashMap;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class JEForm {


	@SecondaryKey(relate=Relationship.ONE_TO_ONE)
	private String formID;
	
	@PrimaryKey
	private String formName;
	
	private HashMap<String, String> columns;

	public String getFormID() {
		return formID;
	}

	public void setFormID(String formID) {
		this.formID = formID;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public HashMap<String, String> getColumns() {
		return columns;
	}

	public void setColumns(HashMap<String, String> columns) {
		this.columns = columns;
	}
	
}
