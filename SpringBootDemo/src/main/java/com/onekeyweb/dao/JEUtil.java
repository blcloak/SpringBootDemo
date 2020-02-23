package com.onekeyweb.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.sleepycat.je.DatabaseException;
import org.springframework.stereotype.Service;

//@Service("jeUtil")
public class JEUtil implements IDBUtil{
	private static final int DEFAULT_PAGE_CNT = 20; 
	private DataAccessor da;

	public JEUtil() {
		this("/Users/yangwei/JEDB");
	}

	public JEUtil(String filePath) {
		File myDbEnvPath = new File(filePath);
		DBEnviment myDbEnv = new DBEnviment(myDbEnvPath, false);
		da = new DataAccessor(myDbEnv.getEntityStore());

	}
	/**
	 * Add data.
	 * @param formName
	 * @param formData
	 * @param formID
	 */
	public void addData(String formName, HashMap<String, String> formData, String formID) {
		if(formID == null) {
			formID = createForm(formName, formData.keySet());
		}
		
		addRecord(formID, formData);
	}
	
	/**
	 * Get data.
	 * @param formID
	 * @param pageNo
	 * @param pageCnt
	 * @param filterCol
	 * @param filterVal
	 * @param filterType
	 * @param orderCol
	 * @param asc
	 * @return
	 */
	public List<HashMap<String, String>> getData(String formID, Integer pageNo, Integer pageCnt, 
												 String filterCol, String filterVal, Integer filterType,
												 String orderCol, boolean asc){
		if(pageCnt == null) pageCnt = DEFAULT_PAGE_CNT;
		if(pageNo == null) pageNo = 1;
		List<String> filterRd = null;
		if(filterCol != null) {
			filterRd = getFilteredRecords(filterCol, filterVal, filterType);
		}
		if(orderCol == null) orderCol = getDefaultOrderCol(formID);
		
		List<String> record = getRecodsByIndex(orderCol, asc, pageCnt, pageNo, filterRd);
		return getRecordData(record);
	}
	
	private List<String> getFilteredRecords(String colID, String colValue, int compareType) {
		JEIndex index = da.indexById.get(colID);
		List<String> record = new ArrayList<>();
		List<List<String>> colIndex = index.getIndex();	
		for(List<String> col : colIndex) {
			if(col.get(0).compareTo(colValue) == compareType) {
				record.add(col.get(1));
			}
		}
		return record;
	}
	
	private List<String> getRecodsByIndex(String colID, boolean asc, int pageCnt, 
			 							  int pageNo, List<String> filteredRd) throws DatabaseException {
		JEIndex index = da.indexById.get(colID);
		List<List<String>> colIndex = index.getIndex();	
		colIndex.sort(getIndexCompare(asc));
		if(filteredRd != null) removeFilterRecord(colIndex, filteredRd);
		List<List<String>> SubList = colIndex.subList((pageNo - 1) * pageCnt, pageCnt * pageNo);
		List<String> record = new ArrayList<>();
		for(List<String> col : SubList) {
			record.add(col.get(1));
		} 
		return record;
	}
	 
	private Comparator<List<String>> getIndexCompare(boolean asc) {
		return new Comparator<List<String>>() {
			public int compare(List<String> s1, List<String> s2) {
				List<String> list1 = (ArrayList<String>) s1;
				List<String> list2 = (ArrayList<String>) s2;
				int compare = list1.get(0).compareTo(list2.get(0)); 
				return asc ? compare : -compare;
			}
		};
	}
	 
	private void removeFilterRecord(List<List<String>> colIndex, List<String> filteredRd) {
		int count = colIndex.size();
		for(int i = count - 1; i >= 0; i--) {
			List<String> index = colIndex.get(i);
			if(!filteredRd.contains(index.get(1))) colIndex.remove(i);
		}
	}

	private List<HashMap<String, String>> getRecordData(List<String> record) {
		List<HashMap<String, String>> oRet =  new ArrayList<>();
		if(record == null) return oRet;
		
		for(String recordID : record) {
			JERecord jeRecord = da.recordById.get(recordID);
			oRet.add(jeRecord.getData());
		}
		return oRet;
	}
	
	private String getDefaultOrderCol(String formID) {
		JEForm form = da.formById.get(formID);
		return form.getColumns().get("DefaultCol");
	}
	
	
	private void addRecord(String formID, HashMap<String, String> formData) {
		JERecord record = new JERecord();
		String recordID = getRecordSequence();
		record.setFromID(formID);
		record.setRecordID(recordID);
		JEForm formIdIdx = da.formById.get(formID);
		HashMap<String, String> columns = formIdIdx.getColumns();
		HashMap<String, String> data = new HashMap<>();
		for(String colName : formData.keySet()) {
			String colID = columns.get(colName);
			String colVal = formData.get(colName);
			data.put(colID, colVal);
			addIndex(formID, colID, colVal, recordID);
		}
		record.setData(data);
		da.recordById.put(record);
	}
	
	private void addIndex(String formID, String colID, String rowID, String rowData) {
		JEIndex index = da.indexById.get(colID);
		List<String> temp = new ArrayList<>();
		temp.add(rowData);
		temp.add(rowID);
		if(index == null) {
			index = new JEIndex();
			index.setColID(colID);
			List<List<String>> data = new ArrayList<>();
			data.add(temp);
			index.setIndex(data);
		}
		List<List<String>> data = index.getIndex();
		data.add(temp);
	}
	
	private String createForm(String formName, Set<String> columns) {
		JEForm form = new JEForm();
		form.setFormName(formName);
		String formID = getFormSequence();
		form.setFormID(formID);
		HashMap<String, String> column = new HashMap<>();
		for(String colName : columns) {
			column.put(colName, getColSequence());
		}
		form.setColumns(column);
		
		da.formByName.put(form);
		
		return formID; 
	}
	
	private String getRecordSequence() {
		long curmil = System.currentTimeMillis();//TODO
		
		return "rd" + curmil;
	}
	
	private String getFormSequence() {
		long curmil = System.currentTimeMillis();//TODO
		
		return "fm" + curmil;
	}
	private String getColSequence() {
		long curmil = System.currentTimeMillis();//TODO
		
		return "cl" + curmil;
	}

}
