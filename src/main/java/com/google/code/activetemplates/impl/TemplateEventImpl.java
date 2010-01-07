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
import com.google.code.activetemplates.EventStream;
import com.google.code.activetemplates.events.Action;
import com.google.code.activetemplates.events.EventComponent;
import com.google.code.activetemplates.events.EventEnvironment;
import com.google.code.activetemplates.events.TemplateEvent;

abstract class TemplateEventImpl implements TemplateEvent, EventStream {

    private CompileContext cc;
    private XMLEvent e;
    private EventComponent ec;

    void init(CompileContext cc, XMLEvent e, EventComponent ec) {
        this.cc = cc;
        this.e = e;
        this.ec = ec;
    }

    public void dispose() {
        cc = null;
        e = null;
    }
    
    public <T> T parseExpression(String expression, Class<T> clazz) {
        return cc.parseExpression(expression, clazz);
    }
    
    public <T> T parseTemplateExpression(String expression, Class<T> clazz) {
        return cc.parseTemplateExpression(expression, clazz);
    }
    
    public <T> T parseExpression(String expression, Object rootObject, Class<T> clazz) {
        return cc.parseExpression(expression, rootObject, clazz);
    }

    public void setExpressionValue(String expression, Object value) {
        cc.setExpressionValue(expression, value);
    }

    public XMLEvent getEvent(){
        return e;
    }

    public XMLEventFactory getEventFactory() {
        return cc.getElementFactory();
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

    public void queueAction(Action a) {

        String aid = registerAction(a);

        cc.queueEvent(com.google.code.activetemplates.lib.elements.ActionEl
                .createActionStartEvent(cc.getElementFactory(), aid));
        cc.queueEvent(com.google.code.activetemplates.lib.elements.ActionEl
                .createActionEndEvent(cc.getElementFactory()));
    }
    
    public String registerAction(Action a) {
        return cc.getActionRegistry().registerAction(a);
    }

    public void executeAction(String aid) {
        Action a = cc.getActionRegistry().removeAction(aid);

        if (a == null)
            throw new IllegalStateException("No such action: " + aid);

        a.execute(this);
    }

    @Override
    public EventStream getEventStream() {
        return this;
    }

    @Override
    public EventEnvironment getEnvironment(){
        return cc.getEventEnvironment();
    }

    public EventComponent getEventComponent() {
        return ec;
    }
    
}