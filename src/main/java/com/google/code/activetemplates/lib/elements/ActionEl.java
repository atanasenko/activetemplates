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

package com.google.code.activetemplates.lib.elements;

import java.util.Collections;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.StartElementEvent;
import com.google.code.activetemplates.impl.handlers.BuiltinHandlerSPI;

public class ActionEl implements ElementHandler {

    public static final QName ELEMENT = new QName(
            BuiltinHandlerSPI.NAMESPACE_STDLIB, "action");
    private static final QName ATTR_AID = new QName("aid");
    
    public QName[] getElements() {
        return new QName[]{ ELEMENT };
    }

    public Outcome processStart(StartElementEvent e) {

        Attribute aid = e.getEvent().getAttributeByName(ATTR_AID);
        if (aid == null)
            throw new IllegalArgumentException("Action aid isn't specified");

        e.executeAction(aid.getValue());

        return Outcome.PROCESS_CHILDREN;
    }

    public Outcome processEnd(EndElementEvent e) {
        return null;
    }

    public static XMLEvent createActionStartEvent(XMLEventFactory f, String aid) {
        Attribute a = f.createAttribute(ATTR_AID, aid);

        return f.createStartElement(ELEMENT, Collections.singleton(a).iterator(),
                Collections.emptySet().iterator());
    }

    public static XMLEvent createActionEndEvent(XMLEventFactory f) {
        return f.createEndElement(ELEMENT, Collections.emptySet().iterator());
    }
}
