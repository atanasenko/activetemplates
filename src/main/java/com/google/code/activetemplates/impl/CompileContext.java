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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.google.code.activetemplates.bind.BindingContext;
import com.google.code.activetemplates.bind.BindingResolverDelegate;
import com.google.code.activetemplates.bind.Bindings;
import com.google.code.activetemplates.script.ScriptingContext;
import com.google.code.activetemplates.script.ScriptingProvider;

public class CompileContext {
    
    private XMLEventReader reader;
    private XMLEventWriter writer;
    private XMLEventFactory elementFactory;
    private ScriptingProvider script;
    private ScriptingContext scriptingContext;
    private Deque<Bindings> bindingStack;
    
    private BindingContext bindingContext;
    
    private Deque<XMLEvent> globalQueue;
    private Deque<XMLEvent> eventQueue;
    private ActionRegistry actionRegistry;

    public CompileContext(XMLEventReader r, XMLEventWriter w, XMLEventFactory ef, ScriptingProvider sc, ScriptingContext sctx, Bindings b) {
        reader = r;
        writer = w;
        elementFactory = ef;
        script = sc;
        scriptingContext = sctx;

        bindingStack = new ArrayDeque<Bindings>();
        globalQueue = new LinkedList<XMLEvent>();
        eventQueue  = new LinkedList<XMLEvent>();
        actionRegistry = new ActionRegistry();
        
        bindingContext = new BindingContext(script, new BindingResolverDelegate());
        pushBindings(b);

    }

    public boolean hasNextEvent(){
        return globalQueue.size() > 0 || reader.hasNext();
    }
    
    public XMLEvent nextEvent() throws XMLStreamException {
        if(globalQueue.size() > 0) return globalQueue.poll();
        return reader.nextEvent();
    }
    
    public XMLEvent peekEvent() throws XMLStreamException {
        if(globalQueue.size() > 0) return globalQueue.peek();
        return reader.peek();
    }
    
    public void queueEvent(XMLEvent e) {
        eventQueue.offer(e);
    }
    
    public void flushEventQueue() {
        while(!eventQueue.isEmpty()) {
            XMLEvent e = eventQueue.pop();
            globalQueue.addLast(e);
        }
    }

    public XMLEventWriter getWriter() {
        return writer;
    }

    public XMLEventFactory getElementFactory() {
        return elementFactory;
    }
    
    public ActionRegistry getActionRegistry() {
        return actionRegistry;
    }

    public ScriptingProvider getScriptingProvider(){
        return script;
    }
    
    public BindingContext getBindingContext() {
        return bindingContext;
    }

    public Bindings getTopLevelBindings() {
        return bindingStack.getLast();
    }
    
    public void pushBindings(Bindings b) {
        if(b == null) {
            throw new NullPointerException("bindings");
        }
        bindingStack.push(b);
        
        bindingContext.setBindings(b);
    }
    
    public void popBindings(){
        if(bindingStack.size() <= 1) {
            throw new IllegalStateException("Cannot end initial scope");
        }
        
        Bindings b = bindingStack.pop();
        bindingContext.setBindings(b);
    }
    
    public ScriptingContext getScriptingContext() {
        return scriptingContext;
    }

}
