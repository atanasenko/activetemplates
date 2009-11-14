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

package com.google.code.activetemplates.events;

import java.util.HashMap;
import java.util.Map;

public class ActionRegistry {
    
    private Map<String, Action> actions;
    
    public ActionRegistry(){
        
    }
    
    public String registerAction(Action a) {
        if(actions == null) actions = new HashMap<String, Action>();
        String aid = Long.valueOf(System.currentTimeMillis()).toString();
        
        actions.put(aid, a);
        
        return aid;
    }
    
    public Action removeAction(String aid){
        if(actions == null) return null;
        
        return actions.remove(aid);
    }

}
