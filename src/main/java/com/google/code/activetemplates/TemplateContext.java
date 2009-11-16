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

package com.google.code.activetemplates;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.google.code.activetemplates.bind.BindingContext;
import com.google.code.activetemplates.events.Action;

/**
 * Each template compile process has a template context associated with it. It
 * provides mid-level access for reading from xml input stream and manipulating
 * data written to output stream.
 * 
 * @author atanasenko
 * 
 */
public interface TemplateContext {

    /**
     * Returns generic XMLEventFactory
     * 
     * @return
     */
    public XMLEventFactory getEventFactory();

    /**
     * Whether or not reader or internal queue have any pending xml events
     * 
     * @return
     */
    public boolean hasNextEvent();

    /**
     * Returns next xml event from reader or internal queue
     * 
     * @return
     * @throws XMLStreamException
     */
    public XMLEvent nextEvent() throws XMLStreamException;

    /**
     * Returns but does not remove next xml event from reader or internal queue.
     * 
     * @return
     * @throws XMLStreamException
     */
    public XMLEvent peekEvent() throws XMLStreamException;

    /**
     * Adds xml event to event queue that will be processed after the current
     * element's processing ends. Subsequent calls to this method from the same
     * event would cause them to be processed in the same order they were added.
     * 
     * @param event
     */
    public void queueEvent(XMLEvent event);

    /**
     * Creates a new action xml event and queues it using queueEvent() method.
     * 
     * @param a
     */
    public void queueAction(Action a);

    /**
     * Executes action identified by specified action id
     * 
     * @param aid
     */
    public void executeAction(String aid);

    /**
     * Returns current binding context.
     * 
     * @return
     */
    public BindingContext getBindingContext();

    /**
     * Starts a new scripting scope
     * 
     * @param topLevel
     */
    public void startScope(boolean topLevel);

    /**
     * Ends current scripting scope
     */
    public void endScope();

}
