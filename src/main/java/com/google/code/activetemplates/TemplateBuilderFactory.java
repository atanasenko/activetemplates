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

public abstract class TemplateBuilderFactory {

    public final static TemplateBuilderFactory newInstance(){
        return Providers.getTemplateSPI().getBuilderFactory();
    }
    public final static TemplateBuilderFactory newInstance(String provider){
        return Providers.getTemplateSPI(provider).getBuilderFactory();
    }
    
    public abstract TemplateBuilder createBuilder(TemplateBuilderConfig config);
}
