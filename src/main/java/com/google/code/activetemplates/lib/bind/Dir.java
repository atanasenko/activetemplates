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

package com.google.code.activetemplates.lib.bind;

import com.google.code.activetemplates.bind.BindingResolutionException;
import com.google.code.activetemplates.bind.BindingResolver;
import com.google.code.activetemplates.bind.Bindings;

public class Dir implements BindingResolver {
    
    public static final String PREFIX = "dir";
    private static final String CRE_PREFIX = "create:";
    
    @Override
    public Object resolve(String value, Bindings b) {
        boolean cre = false;
        if(value.startsWith(CRE_PREFIX)) {
            cre = true;
            value = value.substring(CRE_PREFIX.length());
        }
        java.io.File f = new java.io.File(value);
        if(!f.exists() && cre) {
            if(!f.mkdirs()) {
                throw new BindingResolutionException("Directory " + f.getAbsolutePath() + " cannot be created");
            }
        }
        
        return f;
    }
}
