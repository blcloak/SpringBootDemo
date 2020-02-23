package com.onekeyweb.dao;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class DataAccessor {
	// Open the indices
    public DataAccessor(EntityStore store)
			throws DatabaseException {
    	formByName = store.getPrimaryIndex(String.class, JEForm.class);
    	indexById = store.getPrimaryIndex(String.class, JEIndex.class);
    	recordById = store.getPrimaryIndex(String.class, JERecord.class);
    	recordByForm = store.getSecondaryIndex(recordById, String.class, "fromID");
    	formById = store.getSecondaryIndex(formByName, String.class, "formID");
	}

    PrimaryIndex<String,JEForm> formByName;
    PrimaryIndex<String, JEIndex> indexById;
    PrimaryIndex<String, JERecord> recordById;
    SecondaryIndex<String,String,JERecord> recordByForm;
    SecondaryIndex<String, String, JEForm> formById;
}
