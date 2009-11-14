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

package com.google.code.activetemplates.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;


import com.google.code.activetemplates.events.AttributeHandler;
import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.ElementHandler.Outcome;
import com.google.code.activetemplates.spi.HandlerSPI;
import com.google.code.activetemplates.spi.Providers;

class Handlers {
    
    private Map<QName, AttributeHandler> attributes;
    private Map<QName, ElementHandler> elements;
    private TemplateEventPool eventPool;
    
    public Handlers(){
        
        Map<QName, AttributeHandler> attributes = new HashMap<QName, AttributeHandler>();
        Map<QName, ElementHandler> elements     = new HashMap<QName, ElementHandler>();
        
        List<HandlerSPI> spis = Providers.getHandlerSPIs();
        
        for(HandlerSPI spi: spis) {
            
            if(spi.getAttributeHandlers() != null) {
                attributes.putAll(spi.getAttributeHandlers());
            }
            if(spi.getElementHandlers() != null) {
                elements.putAll(spi.getElementHandlers());
            }
            
        }
        
        this.attributes = attributes;
        this.elements = elements;
        eventPool = new TemplateEventPool();
    }
    
    public boolean isAttributeHandled(QName name){
        return attributes.containsKey(name);
    }
    
    public boolean isElementHandled(QName name){
        return elements.containsKey(name);
    }
    
    public AttributeHandler.Outcome processAttribute(CompileContext cc, Attribute attr) throws XMLStreamException {
        AttributeHandler h = attributes.get(attr.getName());
        if(h == null) throw new IllegalStateException("Attribute " + attr.getName() + " is not handled");
        AttributeEventImpl ev = eventPool.borrowAttributeEvent();
        ev.init(cc, attr);
        try {
            AttributeHandler.Outcome o = h.processAttribute(ev);
            return o != null ? o : AttributeHandler.Outcome.PROCESS_ALL;
        } finally {
            eventPool.returnAttributeEvent(ev);
        }
    }
    
    public ElementHandler.Outcome processStartElement(CompileContext cc, StartElement el) throws XMLStreamException {
        ElementHandler h = elements.get(el.getName());
        if(h == null) throw new IllegalStateException("Element " + el.getName() + " is not handled");
        
        StartElementEventImpl ev = eventPool.borrowStartElementEvent();
        ev.init(cc, el);
        
        try {
            ElementHandler.Outcome o = h.processStart(ev);
            if(o == Outcome.PROCESS_PARENT) {
                throw new IllegalStateException(o + " may not be returned from start element");
            }
            return o != null ? o : Outcome.PROCESS_CHILDREN;
        } finally {
            eventPool.returnStartElementEvent(ev);
        }
    }

    public ElementHandler.Outcome processEndElement(CompileContext cc, EndElement el) throws XMLStreamException {
        ElementHandler h = elements.get(el.getName());
        if(h == null) throw new IllegalStateException("Element " + el.getName() + " is not handled");
        
        EndElementEventImpl ev = eventPool.borrowEndElementEvent();
        ev.init(cc, el);
        
        try {
            ElementHandler.Outcome o = h.processEnd(ev);
            if(o == Outcome.PROCESS_CHILDREN) {
                throw new IllegalStateException(o + " may not be returned from end element");
            }
            return o != null ? o : Outcome.PROCESS_SIBLINGS;
        } finally {
            eventPool.returnEndElementEvent(ev);
        }
    }
}
