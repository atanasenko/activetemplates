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

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.google.code.activetemplates.bind.Bindings;
import com.google.code.activetemplates.script.ScriptingProvider;

/**
 * Base interface for all template events.
 * 
 * Note that TemplateEvent instances might be cached internally, so they
 * should not be used outside of corresponding process method.
 * 
 * @author sleepless
 *
 */
public interface TemplateEvent {

    /**
     * Returns xml event associated with the current template event
     * @return
     */
    public XMLEvent getEvent();
    
    
    /**
     * Returns generic XMLEventFactory
     * @return
     */
    public XMLEventFactory getEventFactory();
    
    /**
     * Whether or not reader or internal queue have any pending xml events
     * @return
     */
    public boolean hasNextEvent();
    
    /**
     * Returns next xml event from reader or internal queue
     * @return
     * @throws XMLStreamException
     */
    public XMLEvent nextEvent() throws XMLStreamException;
    
    /**
     * Pushes xml event onto event queue that will be processed before next event
     * would be read from XMLEventReader 
     * @param event
     */
    public void pushEvent(XMLEvent event);
    
    /**
     * Returns scripting provider instance associated with current compiling
     * context.
     * 
     * @return
     */
    public ScriptingProvider getScriptingProvider();
    
    /**
     * Returns bindings for scripting provider
     * @return
     */
    public Bindings getBindings();
    
    /**
     * Sets new bindings for compilation process
     * @param b
     */
    public void setBindings(Bindings b);
    
}
