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

package com.google.code.activetemplates.script;

import com.google.code.activetemplates.bind.Bindings;

/**
 * Scripting provider is used by template compiler whenever it needs
 * to process any dynamic instructions it encounters.
 * 
 * @author sleepless
 *
 */
public interface ScriptingProvider {
    
    /**
     * Calls specified ScriptingAction providing ScriptingContext
     * @param sa
     */
    public void call(ScriptingAction sa);

    /**
     * Creates new bindings object which maps key-value pairs
     * onto scripting context
     * 
     * @param sc
     * @return
     */
    public Bindings createBindings(ScriptingContext sc);
    
    /**
     * Creates child bindings, which effectively causes a new scripting
     * scope to be created.
     * 
     * @param b
     * @return
     */
    public Bindings createBindings(Bindings b);
    
    /**
     * Evaluates specified script and returns the value it yields.
     * 
     * @param script
     * @param b
     * @return
     */
    public Object eval(String script, Bindings b);

    /**
     * Evaluates script and converts returned value into string.
     * 
     * @param script
     * @param b
     * @return
     */
    public String evalString(String script, Bindings b);
    
    /**
     * Evaluates script and converts returned value into boolean
     * 
     * @param script
     * @param b
     * @return
     */
    public boolean evalBoolean(String script, Bindings b);
    
    /**
     * Compiles specified script to be later executed against bindings object
     * 
     * @param sc
     * @param script
     * @return
     */
    public CompiledScript compile(ScriptingContext sc, String script);
    
}
