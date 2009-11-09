/*
 * Copyright 2009 Anton Tanasenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.activetemplates.bind;

import java.util.Iterator;

/**
 * This class provides base functionality of Bindings contract.
 * Implementors should only provide local context functionality.
 * 
 * @author sleepless
 *
 */
public abstract class AbstractBindings implements Bindings {
    
    protected AbstractBindings parentBindings;
    
    /**
     * Creates a new top-level bindings
     */
    public AbstractBindings(){
        this(null);
    }
    
    /**
     * Create new bindings with a parent bindings object
     * @param parentBindings
     */
    public AbstractBindings(AbstractBindings parentBindings) {
        this.parentBindings = parentBindings;
    }
    
    /**
     * Bind a value in the current context
     */
    public abstract void bind(String name, Object value);
    
    /**
     * Unbind a value in the current context
     */
    public abstract void unbind(String name);
    
    /**
     * Test whether the specified key exists within the current context
     * 
     * @param name
     * @return
     */
    protected abstract boolean containsInternally(String name);
    
    /**
     * Resolve a value within the current context only.
     * 
     * @param name
     * @return
     */
    protected abstract Object resolveInternally(String name);

    /**
     * Return an iterator over all values withing the current context
     * @return
     */
    protected abstract Iterator<?> iterateInternally();
    
    /**
     * Tests whether this binding object is accessible from its children when calling
     * getParent().
     * 
     * This is provided as a helper for implementations which have some top-level
     * bindings object where no values can be bound/unbound from outside.
     * 
     * @return
     */
    protected abstract boolean isAccessible();
    
    public Object lookup(String name) {
        if(containsInternally(name)) {
            return resolveInternally(name);
        }
        if(parentBindings != null) {
            return parentBindings.lookup(name);
        }
        return null;
    }
    
    public <K> K lookup(Class<K> cl) {
        
        Iterator<?> it = iterateInternally();
        while(it.hasNext()) {
            Object o = it.next();
            if(cl.isInstance(o)) {
                return cl.cast(o);
            }
        }
        if(parentBindings != null) {
            return parentBindings.lookup(cl);
        }
        return null;
    }
    
    public <K> K lookup(Class<K> cl, String name) {
        if(containsInternally(name)) {
            Object o = resolveInternally(name);
            if(cl.isInstance(o)) {
                return cl.cast(o);
            }
        }
        if(parentBindings != null) {
            return parentBindings.lookup(cl, name);
        }
        return null;
    }
    
    public Bindings getParent(){
        if(parentBindings == null || !parentBindings.isAccessible())
            return null;
        return parentBindings;
    }
}
