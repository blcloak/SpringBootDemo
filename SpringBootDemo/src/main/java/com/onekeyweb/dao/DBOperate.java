package com.onekeyweb.dao;

import java.io.File;
import java.util.HashMap;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class DBOperate {
	private static Environment myDbEnvironment = null;
	private static Database myDatabase = null;
	
	public static Database getDataBase(String sDBFile) {
		try {
			EnvironmentConfig envConfig = new EnvironmentConfig();
			envConfig.setAllowCreate(true);
			myDbEnvironment = new Environment(new File(sDBFile), envConfig);
			
			DatabaseConfig dbConfig = new DatabaseConfig();
			dbConfig.setAllowCreate(true);
			// Make it a temporary database
			dbConfig.setTemporary(true);
			myDatabase = myDbEnvironment.openDatabase(null, "sampleDatabase", dbConfig); 
			
		} catch (DatabaseException dbe) {
			// Exception handling goes here.
		}
		return myDatabase;
	}
	
	public static void closeDB() {
		try {
			if (myDatabase != null) {
				myDatabase.close();
			}

			if (myDbEnvironment != null) {
				myDbEnvironment.close();
			}
		} catch (DatabaseException dbe) {
			// Exception handling goes here
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void put(String sKey, Object oValue) {
		try {
		    DatabaseEntry theKey = new DatabaseEntry(sKey.getBytes("UTF-8"));
		    HashMap<String, String> oMap = (HashMap<String, String>) oValue;
		    
		    StoredClassCatalog classCatalog = new StoredClassCatalog(myDatabase);
		    EntryBinding dataBinding = new SerialBinding(classCatalog, HashMap.class);
		    DatabaseEntry theData = new DatabaseEntry();

		    dataBinding.objectToEntry(oMap, theData);
		    
		   // DatabaseEntry theData = new DatabaseEntry(oValue.getBytes("UTF-8"));
		    myDatabase.put(null, theKey, theData);
		} catch (Exception e) {
		    // Exception handling goes here
		} 
	}
	
}
