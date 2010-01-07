package com.google.code.activetemplates.events;

/**
 * Representation of the current element environment
 * put() methods add an object to the current element scope, visible within
 * this element and any children.
 * get() methods retrieve an object from within the current element or any ancestors.
 * 
 * @author sleepless
 *
 */
public interface EventEnvironment {

    /**
     * Adds an object which is identified by specified name
     * @param name
     * @param value
     */
    public void put(String name, Object value);
    
    /**
     * Adds and object which is identified by specified class
     * @param <T>
     * @param clazz
     * @param value
     */
    public <T> void put(Class<? extends T> clazz, T value);

    /**
     * Adds an object which is identified by its class
     * @param value
     */
    public void put(Object value);
    
    /**
     * Retrieves an object by specified name
     * @param name
     * @return
     */
    public Object get(String name);
    
    /**
     * Retrieve an object by its name and class
     * @param <T>
     * @param name
     * @param clazz
     * @return
     */
    public <T> T get(String name, Class<T> clazz);

    /**
     * Retrieve and object by its class
     * @param <T>
     * @param clazz
     * @return
     */
    public <T> T get(Class<T> clazz);
}
