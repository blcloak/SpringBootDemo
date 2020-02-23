package com.onekeyweb.dao;

import java.util.HashMap;
import java.util.List;

public interface IDBUtil {
    void addData(String formName, HashMap<String, String> formData, String formID);

    List<HashMap<String, String>> getData(String formID, Integer pageNo, Integer pageCnt,
                                          String filterCol, String filterVal, Integer filterType,
                                          String orderCol, boolean asc);
}
