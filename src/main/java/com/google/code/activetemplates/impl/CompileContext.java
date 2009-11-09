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

import java.util.LinkedList;
import java.util.Queue;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.events.XMLEvent;

import com.google.code.activetemplates.bind.Bindings;
import com.google.code.activetemplates.script.ScriptingProvider;

public class CompileContext {
    
    private XMLEventReader reader;
    private XMLEventWriter writer;
    private XMLEventFactory elementFactory;
    private ScriptingProvider script;
    private Bindings bindings;
    
    private Queue<XMLEvent> eventQueue;

    public CompileContext(XMLEventReader r, XMLEventWriter w, XMLEventFactory ef, ScriptingProvider sc, Bindings b) {
        reader = r;
        writer = w;
        elementFactory = ef;
        script = sc;
        bindings = b;
        
        eventQueue = new LinkedList<XMLEvent>();
    }

    public XMLEventReader getReader() {
        return reader;
    }

    public XMLEventWriter getWriter() {
        return writer;
    }

    public XMLEventFactory getElementFactory() {
        return elementFactory;
    }
    
    public ScriptingProvider getScriptingProvider(){
        return script;
    }
    
    public Bindings getBindings() {
        return bindings;
    }

    public Queue<XMLEvent> getEventQueue(){
        return eventQueue;
    }
    
}
