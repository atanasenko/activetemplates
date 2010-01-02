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

package com.google.code.activetemplates.spi;

import java.util.Collections;
import java.util.List;

import com.google.code.activetemplates.util.Services;

public final class Providers {

    private static final List<TemplateSPI> templateProviders;
    private static final List<HandlerSPI> handlerProviders;

    static {
        templateProviders = Services.getProviders(TemplateSPI.class);
        handlerProviders  = Services.getProviders(HandlerSPI.class);
    }
    
    public final static TemplateSPI getTemplateSPI(){
        if(templateProviders.size() == 0)
            throw new IllegalStateException("No providers registered");
        
        return templateProviders.get(0);
    }
    public final static TemplateSPI getTemplateSPI(String provider){
        if(templateProviders.size() == 0)
            throw new IllegalStateException("No providers registered");
        
        for(TemplateSPI p: templateProviders) {
            if(p.getProviderName().equals(provider)) {
                return p;
            }
        }
        throw new IllegalArgumentException("No such provider: " + provider);
    }
    
    public final static List<HandlerSPI> getHandlerSPIs(){
        return Collections.unmodifiableList(handlerProviders);
    }
    
}
