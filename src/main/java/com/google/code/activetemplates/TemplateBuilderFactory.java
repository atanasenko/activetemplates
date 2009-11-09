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
 * Factory for template builders
 * 
 * @author sleepless
 *
 */
public abstract class TemplateBuilderFactory {

    /**
     * Returns a new default TemplateBuilderFactory
     * @return
     */
    public final static TemplateBuilderFactory newInstance(){
        return Providers.getTemplateSPI().getBuilderFactory();
    }
    
    /**
     * Return a new TemplateBuilderFactory provided by the specified provider
     * 
     * @param provider
     * @return
     */
    public final static TemplateBuilderFactory newInstance(String provider){
        return Providers.getTemplateSPI(provider).getBuilderFactory();
    }
    
    /**
     * Creates a new template builder with a specified configuration
     * 
     * @param config
     * @return
     */
    public abstract TemplateBuilder createBuilder(TemplateBuilderConfig config);
}
