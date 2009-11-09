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

import com.google.code.activetemplates.script.ScriptingProvider;

/**
 * Configuration for creating new TemplateCompilers
 * 
 * @author sleepless
 *
 */
public class TemplateCompilerConfig {
    
    private ScriptingProvider scriptingProvider;

    /**
     * Returns scriptingProvider which is used by template compiler
     * @return
     */
    public ScriptingProvider getScriptingProvider() {
        return scriptingProvider;
    }

    /**
     * Sets a scriptingProvider to be used by template compiler
     * @param scriptingProvider
     */
    public void setScriptingProvider(ScriptingProvider scriptingProvider) {
        this.scriptingProvider = scriptingProvider;
    }
    
}
