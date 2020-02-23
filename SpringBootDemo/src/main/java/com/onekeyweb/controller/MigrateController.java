package com.onekeyweb.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.onekeyweb.dao.JEUtil;

@Controller
public class MigrateController {

    //@Resource
    private JEUtil jeUtil = new JEUtil();
	
	
	@RequestMapping(value = "/migrate/{page}")
    public ModelAndView migrate(ModelAndView modelAndView, @PathVariable("page") String page) {
        modelAndView.setViewName(page);

        return modelAndView;
    }

    @RequestMapping(value = "/save")
    public Map<String, String> save(HashMap<String, String> formData) {
	    String formName = null;
	    String formID = null;

        Iterator<Map.Entry<String, String>> it = formData.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, String> entry = it.next();
            if("jeFomrID".equals(entry.getKey())) {
                formID = entry.getValue();
                it.remove();
            }
            if("jeFomrName".equals(entry.getKey())) {
                formName = entry.getValue();
                it.remove();
            }
        }

        jeUtil.addData(formName, formData, formID);

        return null;
    }


}
