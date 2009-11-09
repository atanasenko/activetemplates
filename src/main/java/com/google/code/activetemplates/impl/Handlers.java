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
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


import com.google.code.activetemplates.bind.Bindings;
import com.google.code.activetemplates.events.AttributeEvent;
import com.google.code.activetemplates.events.AttributeHandler;
import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.StartElementEvent;
import com.google.code.activetemplates.events.TemplateEvent;
import com.google.code.activetemplates.events.ElementHandler.Outcome;
import com.google.code.activetemplates.script.ScriptingProvider;
import com.google.code.activetemplates.spi.HandlerSPI;
import com.google.code.activetemplates.spi.Providers;

public class Handlers {
    
    private Map<QName, AttributeHandler> attributes;
    private Map<QName, ElementHandler> elements;
    
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
    }
    
    public boolean isAttributeHandled(QName name){
        return attributes.containsKey(name);
    }
    
    public boolean isElementHandled(QName name){
        return elements.containsKey(name);
    }
    
    public AttributeHandler.Outcome preProcessAttribute(CompileContext cc, Attribute attr) {
        AttributeHandler h = attributes.get(attr.getName());
        if(h == null) throw new IllegalStateException("Attribute " + attr.getName() + " is not handled");
        AttributeHandler.Outcome o = h.preProcessAttribute(new AttributeEventImpl(cc, attr));
        return o != null ? o : AttributeHandler.Outcome.PROCESS_ATTRIBUTES;
    }
    
    public void postProcessAttribute(CompileContext cc, Attribute attr) {
        AttributeHandler h = attributes.get(attr.getName());
        if(h == null) throw new IllegalStateException("Attribute " + attr.getName() + " is not handled");
        h.postProcessAttribute(new AttributeEventImpl(cc, attr));
    }
    
    public ElementHandler.Outcome processStartElement(CompileContext cc, StartElement el) {
        ElementHandler h = elements.get(el.getName());
        if(h == null) throw new IllegalStateException("Element " + el.getName() + " is not handled");
        ElementHandler.Outcome o = h.processStart(new StartElementEventImpl(cc, el));
        if(o == Outcome.PROCESS_PARENT) {
            throw new IllegalStateException(o + " may not be returned from start element");
        }
        return o != null ? o : Outcome.PROCESS_CHILDREN;
    }

    public ElementHandler.Outcome processEndElement(CompileContext cc, EndElement el) {
        ElementHandler h = elements.get(el.getName());
        if(h == null) throw new IllegalStateException("Element " + el.getName() + " is not handled");
        ElementHandler.Outcome o = h.processEnd(new EndElementEventImpl(cc, el));
        if(o == Outcome.PROCESS_CHILDREN) {
            throw new IllegalStateException(o + " may not be returned from end element");
        }
        return o != null ? o : Outcome.PROCESS_SIBLINGS;
    }
    
    private abstract class EventImpl implements TemplateEvent {
        
        private CompileContext cc;
        private XMLEvent e;
        
        EventImpl(CompileContext cc, XMLEvent e) {
            this.cc = cc;
            this.e = e;
        }

        public Bindings getBindings() {
            // TODO
            return null;
        }

        public XMLEventReader getEventReader() {
            return cc.getReader();
        }

        public XMLEventWriter getEventWriter() {
            return cc.getWriter();
        }

        public XMLEventFactory getElementFactory() {
            return cc.getElementFactory();
        }
        
        public ScriptingProvider getScriptingProvider(){
            return cc.getScriptingProvider();
        }
        
        public XMLEvent getEvent(){
            return e;
        }

    }
    
    private class AttributeEventImpl extends EventImpl implements AttributeEvent {
        
        AttributeEventImpl(CompileContext cc, Attribute e) {
            super(cc, e);
        }

        @Override
        public Attribute getEvent() {
            return (Attribute) super.getEvent();
        }
    }
    
    private class StartElementEventImpl extends EventImpl implements StartElementEvent {

        StartElementEventImpl(CompileContext cc, StartElement e) {
            super(cc, e);
        }
        
        public StartElement getEvent() {
            return super.getEvent().asStartElement();
        }
    }
    
    private class EndElementEventImpl extends EventImpl implements EndElementEvent {

        EndElementEventImpl(CompileContext cc, EndElement e) {
            super(cc, e);
        }
        
        public EndElement getEvent() {
            return super.getEvent().asEndElement();
        }
    }}
