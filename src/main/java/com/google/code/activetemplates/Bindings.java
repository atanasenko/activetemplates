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

package com.google.code.activetemplates;

import java.util.LinkedHashMap;
import java.util.Map;

public class Bindings {
    
    protected Bindings parentBindings;
    protected Map<String, Object> data;
    
    public Bindings(){
        this(null);
    }
    
    public Bindings(Bindings parentBindings) {
        this.parentBindings = parentBindings;
        data = new LinkedHashMap<String, Object>();
    }
    
    public void bind(String name, Object value) {
        data.put(name, value);
    }
    
    public void unbind(String name) {
        data.remove(name);
    }
    
    public Object resolve(String name) {
        if(data.containsKey(name)) {
            return data.get(name);
        }
        if(parentBindings != null) {
            return parentBindings.resolve(name);
        }
        return null;
    }
    
    public <K> K resolve(Class<K> cl) {
        
        for(Object o: data.values()) {
            if(cl.isInstance(o)) {
                return cl.cast(o);
            }
        }
        if(parentBindings != null) {
            return parentBindings.resolve(cl);
        }
        return null;
    }
    
    public <K> K resolve(Class<K> cl, String name) {
        if(data.containsKey(name)) {
            Object o = data.get(name);
            if(cl.isInstance(o)) {
                return cl.cast(o);
            }
        }
        if(parentBindings != null) {
            return parentBindings.resolve(cl, name);
        }
        return null;
    }
}
