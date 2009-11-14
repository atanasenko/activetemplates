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

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.google.code.activetemplates.bind.Bindings;
import com.google.code.activetemplates.events.TemplateEvent;
import com.google.code.activetemplates.script.ScriptingProvider;

abstract class TemplateEventImpl implements TemplateEvent {
    
    private CompileContext cc;
    private XMLEvent e;
    
    void init(CompileContext cc, XMLEvent e) {
        this.cc = cc;
        this.e = e;
    }

    public void dispose() {
        cc = null;
        e = null;
    }

    public Bindings getBindings() {
        return cc.getBindings();
    }
    
    public void setBindings(Bindings b) {
        cc.setBindings(b);
    }

    public XMLEventFactory getEventFactory() {
        return cc.getElementFactory();
    }
    
    public ScriptingProvider getScriptingProvider(){
        return cc.getScriptingProvider();
    }
    
    public boolean hasNextEvent() {
        return cc.hasNextEvent();
    }

    public XMLEvent nextEvent() throws XMLStreamException {
        return cc.nextEvent();
    }

    public XMLEvent peekEvent() throws XMLStreamException {
        return cc.peekEvent();
    }

    public void queueEvent(XMLEvent e) {
        cc.queueEvent(e);
    }

    public XMLEvent getEvent(){
        return e;
    }

}