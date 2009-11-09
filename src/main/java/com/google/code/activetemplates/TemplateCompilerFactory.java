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

import com.google.code.activetemplates.spi.Providers;

/**
 * Factory for template compilers
 * 
 * @author sleepless
 *
 */
public abstract class TemplateCompilerFactory {
    
    /**
     * Returns a new default TemplateCompilerFactory
     * 
     * @return
     */
    public final static TemplateCompilerFactory newInstance(){
        return Providers.getTemplateSPI().getCompilerFactory();
    }
    
    /**
     * Returns a new templateCompilerFactory provided by the specified provider.
     * 
     * @param provider
     * @return
     */
    public final static TemplateCompilerFactory newInstance(String provider){
        return Providers.getTemplateSPI(provider).getCompilerFactory();
    }
    
    /**
     * Creates a new compiler
     * 
     * @return
     */
    public abstract TemplateCompiler createCompiler(TemplateCompilerConfig conf);

}
