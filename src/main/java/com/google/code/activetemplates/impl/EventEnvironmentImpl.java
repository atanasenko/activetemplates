package com.google.code.activetemplates.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.code.activetemplates.events.EventEnvironment;

class EventEnvironmentImpl implements EventEnvironment {

    private EventEnvironmentImpl parent;
    private Map<String, Object> data;
    private int innerCount;
    
    public EventEnvironmentImpl() {
        this(null);
    }
    
    public EventEnvironmentImpl(EventEnvironmentImpl parent) {
        this.parent = parent;
        innerCount = 0;
    }
    
    @Override
    public Object get(String name) {
        if(data == null || !data.containsKey(name)) {
            return parent == null ? null : parent.get(name);
        }
        return data.get(name);
    }

    @Override
    public <T> T get(String name, Class<T> clazz) {
        return clazz.cast(get(name));
    }

    @Override
    public void put(String name, Object value) {
        if(data == null) {
            data = new HashMap<String, Object>();
        }
        data.put(name, value);
    }

    public int getInnerCount() {
        return innerCount;
    }
    
    public void incInnerCount() {
        innerCount++;
    }
    
    public void decInnerCount() {
        innerCount--;
    }

}
