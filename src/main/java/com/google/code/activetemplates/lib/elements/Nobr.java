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

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.StartElementEvent;
import com.google.code.activetemplates.impl.handlers.DefaultHandlerSPI;

/**
 * Causes next 'characters' element to be stripped of whitespaces at the beginning
 * @author sleepless
 *
 */
public class Nobr implements ElementHandler {

    public static final QName TAG = new QName(DefaultHandlerSPI.NAMESPACE_STDLIB, "nobr");

    public Outcome processStart(StartElementEvent e) {
        return null;
    }

    public Outcome processEnd(EndElementEvent e) throws XMLStreamException {
        XMLEvent ne = e.nextEvent();
        
        if(ne.isCharacters()) {
            Characters c = ne.asCharacters();
            String d = c.getData().replaceAll("^[\\s]*", "");
            e.queueEvent(e.getEventFactory().createCharacters(d));
        } else {
            e.queueEvent(ne);
        }
        return null;
    }

}