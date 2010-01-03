package com.google.code.activetemplates.events;

public interface EventEnvironment {

    public void put(String name, Object value);
    
    public Object get(String name);
    
    public <T> T get(String name, Class<T> clazz);
}
