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

import com.google.code.activetemplates.bind.Bindings;
import com.google.code.activetemplates.script.Script;
import com.google.code.activetemplates.script.ScriptingProvider;

/**
 * Scripting provider which uses mozilla rhino api directly.
 * 
 * @author sleepless
 *
 */
public class RhinoScriptingProvider implements ScriptingProvider {
    
    public RhinoScriptingProvider(){
        // TODO take base scripts as an argument
    }

    @Override
    public Script compile(String script) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bindings createBindings() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Bindings createBindings(Bindings b) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object eval(String script, Bindings b) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean evalBoolean(String script, Bindings b) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String evalString(String script, Bindings b) {
        // TODO Auto-generated method stub
        return null;
    }

}
