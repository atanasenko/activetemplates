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

import com.google.code.activetemplates.script.ScriptingProvider;

/**
 * Context object used in binding resolution
 * @author sleepless
 *
 */
public class BindingContext {
    
    private Bindings bindings;
    private ScriptingProvider scriptingProvider;
    private BindingResolver bindingResolver;
    
    /**
     * Creates new binding context
     * @param scriptingProvider
     */
    public BindingContext(ScriptingProvider scriptingProvider, BindingResolver bindingResolver) {
        this.scriptingProvider = scriptingProvider;
        this.bindingResolver = bindingResolver;
    }

    /**
     * Get bindings associated with this binding context
     * @return
     */
    public Bindings getBindings() {
        return bindings;
    }
    
    /**
     * Sets new bindings
     * @param bindings
     */
    public void setBindings(Bindings bindings) {
        this.bindings = bindings;
    }

    /**
     * Get scripting provider associated with this binding context
     * @return
     */
    public ScriptingProvider getScriptingProvider() {
        return scriptingProvider;
    }

    /**
     * Get binding resolver associated with this binding context
     * @return
     */
    public BindingResolver getBindingResolver() {
        return bindingResolver;
    }

}
