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

package com.google.code.activetemplates.exp;

import java.util.ArrayList;
import java.util.List;

import com.google.code.activetemplates.bind.BindingContext;

/**
 * Expansion that contains other expansions inside.
 * Resolution will be done sequentially
 * 
 * @author sleepless
 *
 */
public class CompoundExpansion implements Expansion {
    
    private List<Expansion> expansions;
    
    public CompoundExpansion(){
        expansions = new ArrayList<Expansion>();
    }
    
    public void addExpansion(Expansion e) {
        expansions.add(e);
    }
    
    public List<Expansion> getExpansions() {
        return expansions;
    }

    public void resolve(StringBuilder sb, BindingContext bc) {
        for(Expansion e: expansions) {
            e.resolve(sb, bc);
        }
    }
    
    public String toString() {
        return expansions.toString();
    }

}
