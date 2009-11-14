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

import java.util.Map;

import com.google.code.activetemplates.spi.Providers;

/**
 * Binding resolver which delegates resolution process based on
 * binding prefix
 * 
 * @author sleepless
 *
 */
public class BindingResolverDelegate implements BindingResolver {

    private Map<String, BindingResolver> resolvers;
    
    public BindingResolverDelegate(){
        resolvers = Providers.getBindingSPI().getResolvers();
    }
    
    @Override
    public Object resolve(String prefix, String value, BindingContext bc) {
        
        BindingResolver br = resolvers.get(prefix);
        if(br == null){
            throw new IllegalArgumentException("No such resolver: " + prefix);
        }
        
        return br.resolve(prefix, value, bc);
    }
    
    public Object resolve(String value, BindingContext bc) {
        
        String[] parts = value.split(":", 2);
        String prefix = parts.length == 2 ? parts[0].trim() : "";
        String expr = parts.length == 2 ? parts[1].trim() : parts[0].trim();
        
        return resolve(prefix, expr, bc);
    }

}
