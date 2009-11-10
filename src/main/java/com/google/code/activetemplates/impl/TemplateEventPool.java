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

package com.google.code.activetemplates.impl;

import org.apache.commons.pool.KeyedObjectPool;
import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;

class TemplateEventPool {

    private KeyedObjectPool pool;
    
    public TemplateEventPool(){
        pool = new GenericKeyedObjectPool(new TemplateEventFactory());
    }
    
    public AttributeEventImpl borrowAttributeEvent(){
        try { return (AttributeEventImpl) pool.borrowObject(AttributeEventImpl.class); } 
        catch(Exception e){ throw new IllegalStateException(e); }
    }
    
    public StartElementEventImpl borrowStartElementEvent(){
        try { return (StartElementEventImpl) pool.borrowObject(StartElementEventImpl.class); } 
        catch(Exception e){ throw new IllegalStateException(e); }
    }
    
    public EndElementEventImpl borrowEndElementEvent(){
        try { return (EndElementEventImpl) pool.borrowObject(EndElementEventImpl.class); } 
        catch(Exception e){ throw new IllegalStateException(e); }
    }
    
    public void returnAttributeEvent(AttributeEventImpl ev) {
        try{ pool.returnObject(AttributeEventImpl.class, ev); }
        catch(Exception e){ throw new IllegalStateException(e); }
    }
    
    public void returnStartElementEvent(StartElementEventImpl ev) {
        try{ pool.returnObject(StartElementEventImpl.class, ev); }
        catch(Exception e){ throw new IllegalStateException(e); }
    }

    public void returnEndElementEvent(EndElementEventImpl ev) {
        try{ pool.returnObject(EndElementEventImpl.class, ev); }
        catch(Exception e){ throw new IllegalStateException(e); }
    }

    private static class TemplateEventFactory implements KeyedPoolableObjectFactory {

        public Object makeObject(Object key) throws Exception {
            @SuppressWarnings("unchecked")
            Class<? extends TemplateEventImpl> cl = (Class<? extends TemplateEventImpl>) key;
            return cl.newInstance();
        }

        public void activateObject(Object key, Object obj) throws Exception {
        }

        public boolean validateObject(Object key, Object obj) {
            return true;
        }
        
        public void passivateObject(Object key, Object obj) throws Exception {
            ((TemplateEventImpl)obj).dispose();
        }

        public void destroyObject(Object key, Object obj) throws Exception {
        }

    }
    
}
