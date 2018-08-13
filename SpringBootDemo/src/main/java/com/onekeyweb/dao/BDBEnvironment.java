package com.onekeyweb.dao;

import java.io.File;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.StoreConfig;

public class BDBEnvironment {
	private Environment env;
	private StoreConfig storeConfig;
	private String envHome;
	
	public BDBEnvironment(String envHome){
		System.out.println("envHome:" + envHome);
		this.envHome = envHome;
		init();
	}
	
	public BDBEnvironment(String envHome, boolean deleteFolderContents){
		System.out.println("envHome:" + envHome);
		this.envHome = envHome;
		if(deleteFolderContents) {
			JFileUtils.deleteFolderContents(new File(envHome));
		} 
		init();
	}
	
	public void init() {
		
		EnvironmentConfig myEnvConfig = new EnvironmentConfig();
        myEnvConfig.setReadOnly(false);
        myEnvConfig.setTransactional(true);
        myEnvConfig.setAllowCreate(true);
        
        storeConfig = new StoreConfig();
        storeConfig.setReadOnly(false);
        storeConfig.setAllowCreate(true);
        storeConfig.setTransactional(true);
        env = new Environment(new File(envHome), myEnvConfig);
		
	}
	
	public Environment getEnv() {
		return env;
	}
	public void setEnv(Environment env) {
		this.env = env;
	}
	public StoreConfig getStoreConfig() {
		return storeConfig;
	}
	public void setStoreConfig(StoreConfig storeConfig) {
		this.storeConfig = storeConfig;
	}

}
