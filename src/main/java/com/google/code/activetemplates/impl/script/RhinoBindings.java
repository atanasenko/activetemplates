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

package com.google.code.activetemplates.impl.script;

import java.util.Iterator;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.google.code.activetemplates.bind.AbstractBindings;

class RhinoBindings extends AbstractBindings {
    
    private RhinoScriptingContext ctx;
    private Scriptable scope;
    
    public RhinoBindings(RhinoScriptingContext ctx, Scriptable scope, RhinoBindings parentBindings) {
        super(parentBindings);
        this.ctx = ctx;
        this.scope = scope;
    }
    
    public Scriptable getScope(){
        return scope;
    }
    
    public RhinoScriptingContext getScriptingContext(){
        return ctx;
    }

    public void bind(String name, Object value) {
        value = Context.javaToJS(value, scope);
        scope.put(name, scope, value);
    }

    public void unbind(String name) {
        scope.delete(name);
    }

    protected boolean containsInternally(String name) {
        return scope.has(name, scope);
    }

    protected Object resolveInternally(String name) {
        return Context.jsToJava(scope.get(name, scope), Object.class);
    }

    protected Iterator<?> iterateInternally() {
        return new ScopeIterator();
    }

    protected boolean isAccessible() {
        return true;
    }
    
    private class ScopeIterator implements Iterator<Object> {
        
        private int idx;
        private Object[] keys;
        
        ScopeIterator(){
            idx = 0;
            keys = scope.getIds();
        }
        
        public boolean hasNext() {
            return idx < keys.length;
        }

        public Object next() {
            Object key = keys[idx++];
            return scope.get(key.toString(), scope);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
