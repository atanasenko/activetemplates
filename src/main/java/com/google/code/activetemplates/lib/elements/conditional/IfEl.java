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

package com.google.code.activetemplates.lib.elements.conditional;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;

import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.StartElementEvent;
import com.google.code.activetemplates.impl.handlers.BuiltinHandlerSPI;

/**
 * Processes element children only if name attribute evaluates to true
 * 
 * @author sleepless
 * 
 */
public class IfEl extends ConditionalEl {

    public static final QName ELEMENT = new QName(
            BuiltinHandlerSPI.NAMESPACE_STDLIB, "if");

    private static final QName ATTR_CONDITION = new QName("condition");

    public QName[] getElements() {
        return new QName[]{ ELEMENT };
    }

    public Outcome processStart(StartElementEvent e) {

        Attribute a = e.getEvent().getAttributeByName(ATTR_CONDITION);
        if (a == null) {
            throw new IllegalStateException("Condition not specified");
        }
        boolean cond = e.parseExpression(a.getValue(), Boolean.class).booleanValue();
        
        // create new condition scope or use existing, if non inner
        ConditionScope cScope = getConditionScope(e);
        if(cScope == null || cScope.isInner()) {
            newConditionScope(e);
        }
        
        // do processing
        return processIf(e, cond);
    }

    public void processEnd(EndElementEvent e) {
        processEndIf(e);
    }

}
