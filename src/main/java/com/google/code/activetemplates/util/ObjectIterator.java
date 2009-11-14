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

package com.google.code.activetemplates.util;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility class which can iterate over Iterable, Map, Array or a single object
 * 
 * @author sleepless
 *
 */
public abstract class ObjectIterator {
    
    /**
     * Switch context to next element, returns true if there is any, false if end 
     * of iterator is reached.
     * @return
     */
    public abstract boolean next();
    
    /**
     * Gets the index into iteration.
     * 
     * @return
     */
    public abstract int getIndex();
    
    /**
     * Returns key of the map element or index, if not iterating over map.
     * @return
     */
    public abstract Object getKey();
    
    /**
     * Gets the current object
     * @return
     */
    public abstract Object getObject();
    
    /**
     * Creates a new ObjectIterator
     * 
     * @param data
     * @return
     */
    public static final ObjectIterator create(Object data) {
        if(data instanceof Map<?, ?>) {
            @SuppressWarnings("unchecked")
            Map<Object, Object> map = (Map<Object, Object>)data;
            return new MapIterator(map);
        } else if(data instanceof Iterable<?>) {
            return new CollectionIterator((Iterable<?>)data);
        } else if(data.getClass().isArray()) {
            return new ArrayIterator(data);
        } else {
            return new CollectionIterator(Collections.singleton(data));
        }
    }
    
    private static class CollectionIterator extends ObjectIterator {
        private Iterator<?> it;
        private Object o;
        private int index;
        
        public CollectionIterator(Iterable<?> iterable) {
            this.it = iterable.iterator();
            index = -1;
        }

        public boolean next() {
            if(!it.hasNext()) return false;
            o = it.next();
            index++;
            return true;
        }
        
        public int getIndex() {
            return index;
        }

        public Object getKey() {
            return index;
        }

        public Object getObject() {
            return o;
        }
    }
    
    private static class ArrayIterator extends ObjectIterator {
        private Object array;
        private Object o;
        private int index;
        
        public ArrayIterator(Object array) {
            this.array = array;
            index = -1;
        }

        public boolean next() {
            if(Array.getLength(array) <= index) return false;
            index++;
            o = Array.get(array, index);
            return true;
        }
        
        public int getIndex() {
            return index;
        }

        public Object getKey() {
            return index;
        }

        public Object getObject() {
            return o;
        }
    }
    
    private static class MapIterator extends ObjectIterator {
        
        private Iterator<Map.Entry<Object, Object>> it;
        private Map.Entry<Object, Object> e;
        private int index;
        
        public MapIterator(Map<Object, Object> map) {
            it = map.entrySet().iterator();
            index = -1;
        }

        public boolean next() {
            if(!it.hasNext()) return false;
            e = it.next();
            index++;
            return true;
        }
        
        public int getIndex() {
            return index;
        }

        public Object getKey() {
            return e.getKey();
        }

        public Object getObject() {
            return e.getValue();
        }
    }
    
}
