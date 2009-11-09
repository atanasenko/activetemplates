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

package org.sleepless.at.lib.elements;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;

import org.sleepless.at.events.ElementHandler;
import org.sleepless.at.events.EndElementEvent;
import org.sleepless.at.events.StartElementEvent;

public class If implements ElementHandler {
    
    public static final QName TAG = new QName("if");
    
    private static final QName ATTR_CONDITION = new QName("condition");

    public Outcome processStart(StartElementEvent e) {
        
        String v;
        Attribute a = e.getEvent().getAttributeByName(ATTR_CONDITION);
        v = a != null ? a.getValue() : null;
        
        if(!Boolean.parseBoolean(v)) {
            return Outcome.PROCESS_SIBLINGS;
        }
        
        return null;
    }

    public Outcome processEnd(EndElementEvent e) {
        return null;
    }

}
