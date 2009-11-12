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

import org.mozilla.javascript.Context;

import com.google.code.activetemplates.script.ScriptingContext;

class RhinoScriptingContext implements ScriptingContext {
    
    private Context context;
    private String location;
    private volatile boolean closed;

    public RhinoScriptingContext(Context context) {
        this.context = context;
        closed = false;
    }

    public Context getContext() {
        if(closed) throw new IllegalStateException("This scripting context is closed");
        return context;
    }
    
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isClosed() {
        return closed;
    }

    public void close() {
        closed = true;
    }
    
}
