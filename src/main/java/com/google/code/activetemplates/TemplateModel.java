package com.google.code.activetemplates;

import java.util.Map;

public class TemplateModel {

    private Map<String, ?> data;

    public TemplateModel(Map<String, ?> data) {
        this.data = data;
    }

    public Object get(Object key){
        return data.get(key);
    }
    
}
