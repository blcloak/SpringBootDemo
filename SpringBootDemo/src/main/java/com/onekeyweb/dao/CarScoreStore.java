package com.onekeyweb.dao;

import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.ForwardCursor;
import com.sleepycat.je.LockConflictException;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.Transaction;
import com.sleepycat.je.TransactionConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.EntityJoin;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class CarScoreStore extends AbstractStore {
	public PrimaryIndex<String, CarScore> carScoreById;
	public SecondaryIndex<String, String, CarScore> carScoreByBrand;
	public SecondaryIndex<String, String, CarScore> carScoreBySeries;
 
	public SecondaryIndex<String, String, CarScore> carScoreByType;
	//public SecondaryIndex<String, String, CarScore> carScoreByYear;
	
	
	//-----Construction Method------
	public CarScoreStore(BDBEnvironment env) {
		
		super(env);
		
		carScoreById = store.getPrimaryIndex(String.class, CarScore.class); ///primary key
    	carScoreByBrand = store.getSecondaryIndex(carScoreById, String.class, "brand");
    	carScoreBySeries = store.getSecondaryIndex(carScoreById, String.class, "series");
 
    	carScoreByType = store.getSecondaryIndex(carScoreById, String.class, "carType");
    	//carScoreByYear = store.getSecondaryIndex(carScoreById, String.class, "year");
	}
 
	//-------Operation Method--------
 
	
	/**
     *  add a record
     * 
     * **/
    public void put(CarScore entity) {
 
		boolean retry = true;
		int retry_count = 0;
		
        while (retry) {
        	Transaction txn = store.getEnvironment().beginTransaction(null, null);
			carScoreById.put(txn, entity);
			try {
				try {
					txn.commit();
					txn = null;
				} catch (DatabaseException e) {
					System.err.println("Error on txn commit: " + e.toString());
				}
				retry = false;
			} catch (LockConflictException de) {
				System.out.println("BDB: Deadlock");
				
				if (retry_count < MAX_RETRY) {  // retry if necessary
					retry = true;
					retry_count++;
				} else {
					System.err.println("BDB: Out of retries[put]. Giving up.");
					retry = false;
				}
			} catch (DatabaseException e) {
				retry = false;  // abort and don't retry
				System.err.println("BDB exception: " + e.toString());
				e.printStackTrace();
			} finally {
				if (txn != null) {
					try {
						txn.abort();
					} catch (Exception e) {
						System.err.println("Error aborting transaction: " + e.toString());
						e.printStackTrace();
					}
				}
			}
		}
    }
	
	/**
     *  add a record
     * 
     * **/
    public void delete(String carId) {
 
		boolean retry = true;
		int retry_count = 0;
		
        while (retry) {
        	Transaction txn = store.getEnvironment().beginTransaction(null, null);
			carScoreById.delete(txn, carId);
			try {
				try {
					txn.commit();
					txn = null;
				} catch (DatabaseException e) {
					System.err.println("Error on txn commit: " + e.toString());
				}
				retry = false;
			} catch (LockConflictException de) {
				System.out.println("BDB: Deadlock");
				
				if (retry_count < MAX_RETRY) {  // retry if necessary
					retry = true;
					retry_count++;
				} else {
					System.err.println("BDB: Out of retries[put]. Giving up.");
					retry = false;
				}
			} catch (DatabaseException e) {
				retry = false;  // abort and don't retry
				System.err.println("BDB exception: " + e.toString());
				e.printStackTrace();
			} finally {
				if (txn != null) {
					try {
						txn.abort();
					} catch (Exception e) {
						System.err.println("Error aborting transaction: " + e.toString());
						e.printStackTrace();
					}
				}
			}
		}
    }
 
    
	/**
	 * search BDBItem by id
	 * 
	 * **/
	public CarScore get(String carId){
		
		boolean retry = true;
		int retry_count = 0;
		TransactionConfig txnConfig = new TransactionConfig();
	    txnConfig.setReadCommitted(true);
	    CarScore carScore = null;
        while (retry) {
        	
        	Transaction txn = store.getEnvironment().beginTransaction(null, txnConfig);
    		carScore = carScoreById.get(txn, carId, LockMode.READ_COMMITTED);
			try {
				try {
					txn.commit();
					txn = null;
				} catch (DatabaseException e) {
					System.err.println("Error on txn commit: " + e.toString());
				}
				retry = false;
			} catch (LockConflictException de) {
				System.out.println("BDB: Deadlock");
				// retry if necessary
				if (retry_count < MAX_RETRY) {
					retry = true;
					retry_count++;
				} else {
					System.err.println("BDB: Out of retries[get]. Giving up.");
					retry = false;
				}
			} catch (DatabaseException e) {
				
				retry = false;   // abort and don't retry
				System.err.println("BDB exception: " + e.toString());
				e.printStackTrace();
			} finally {
				if (txn != null) {
					try {
						txn.abort();
					} catch (Exception e) {
						System.err.println("Error aborting transaction: " + e.toString());
						e.printStackTrace();
					}
				}
			}
		}
		return carScore;
	}
    
	/**
	 * entity cursor
	 * 
	 * **/
	public EntityCursor<CarScore> entityCursorGet(){
 
		Transaction txn = store.getEnvironment().beginTransaction(null, null);
	    CursorConfig cconfig = new CursorConfig();
	    cconfig.setReadCommitted(true);
	    return carScoreById.entities(txn, cconfig);
	}
	
	/**
	 * key cursor
	 * 
	 * **/
	public EntityCursor<String> keyCursorGet(){
 
		Transaction txn = store.getEnvironment().beginTransaction(null, null);
	    CursorConfig cconfig = new CursorConfig();
	    cconfig.setReadCommitted(true);
	    
	    //放入map
	    EntityCursor<String> e = carScoreById.keys(txn, cconfig);
	    cursorTransactionMap.put(e, txn);
	    
	    return e;
	}
	
	
	
	/**
	 * 根据brand来找
	 * @param tag
	 * @return
	 */
	public EntityCursor<CarScore> findByBrand(String brand){
		EntityIndex<String, CarScore> e = carScoreByBrand.subIndex(brand);
		Transaction txn = store.getEnvironment().beginTransaction(null, null);
		CursorConfig cconfig = new CursorConfig();
		cconfig.setReadCommitted(true);
		
		//放入map
	    EntityCursor<CarScore> ec = e.entities(txn, cconfig);
	    cursorTransactionMap.put(ec, txn);
		
		return ec;
	}
	
	/**
	 * 根据brand来找
	 * @param tag
	 * @return
	 */
	public EntityCursor<CarScore> findBySeries(String series) {
		EntityIndex<String, CarScore> e = carScoreBySeries.subIndex(series);
		Transaction txn = store.getEnvironment().beginTransaction(null, null);
		CursorConfig cconfig = new CursorConfig();
		cconfig.setReadCommitted(true);
		
		//放入map
	    EntityCursor<CarScore> ec = e.entities(txn, cconfig);
	    cursorTransactionMap.put(ec, txn);
		
		return ec;
	}
	
 
	
	/**
	 * 取到所有tag
	 * @return
	 */
	public EntityCursor<String> getAllTags() {
		Transaction txn = store.getEnvironment().beginTransaction(null, null);
		CursorConfig cconfig = new CursorConfig();
		cconfig.setReadCommitted(true);
		
		
		//放入map
	    EntityCursor<String> ec = carScoreByBrand.keys(txn, cconfig);
	    cursorTransactionMap.put(ec, txn);
	    
		return ec;
	}
 
    public long getAllCount(){
    	return carScoreById.count();
    }
    
 
	//----------------------EntityJoin------------------------
	/**
	 * 统一二级索引查询：join搜索
	 * @param type
	 * @param year
	 * @return
	 */
	public ForwardCursor<CarScore> getJoinEntity(String brand, String series, String type, String year) {
		
		Transaction txn = store.getEnvironment().beginTransaction(null, null);
		CursorConfig cconfig = new CursorConfig();
		cconfig.setReadCommitted(true);
		
		EntityJoin<String, CarScore> join = new EntityJoin<String, CarScore>(carScoreById);
		if(brand != null){
			join.addCondition(carScoreByBrand, brand);
		}
		if(series != null){
			join.addCondition(carScoreBySeries, series);
		}
		if(type != null) {
			join.addCondition(carScoreByType, type);
		}
		
		return join.entities(txn, cconfig);
	}

}
