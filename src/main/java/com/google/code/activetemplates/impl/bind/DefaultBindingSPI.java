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

package com.google.code.activetemplates.impl.bind;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.code.activetemplates.bind.BindingResolver;
import com.google.code.activetemplates.lib.bind.Clazz;
import com.google.code.activetemplates.lib.bind.Dir;
import com.google.code.activetemplates.lib.bind.File;
import com.google.code.activetemplates.lib.bind.Literal;
import com.google.code.activetemplates.lib.bind.Script;
import com.google.code.activetemplates.spi.BindingSPI;

public class DefaultBindingSPI implements BindingSPI {
    
    private static final Map<String, BindingResolver> resolvers = new HashMap<String, BindingResolver>();
    
    static {
        
        resolvers.put(Script.PREFIX, new Script());
        resolvers.put(Clazz.PREFIX, new Clazz());
        resolvers.put(Dir.PREFIX, new Dir());
        resolvers.put(File.PREFIX, new File());
        resolvers.put(Literal.PREFIX, new Literal());
        
    }

    public String getProviderName() {
        return "default";
    }

    public Map<String, BindingResolver> getResolvers() {
        return Collections.unmodifiableMap(resolvers);
    }
    
}
