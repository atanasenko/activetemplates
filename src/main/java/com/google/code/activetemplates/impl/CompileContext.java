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

import java.util.Deque;
import java.util.LinkedList;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;

class CompileContext {
    
    private static final ParserContext TEMPLATE_PARSER_CONTEXT = 
        new TemplateParserContext("${", "}");

    private XMLEventReader reader;
    private XMLEventWriter writer;
    private XMLEventFactory elementFactory;
    
    private Deque<XMLEvent> globalQueue;
    private Deque<XMLEvent> eventQueue;
    private Deque<EventEnvironmentImpl> env;

    private EventComponentFactory eComponentFactory;

    private ActionRegistry actionRegistry;
    private ExpressionParser expressionParser;
    private EvaluationContext evaluationContext;
    
    public CompileContext(XMLEventReader r, XMLEventWriter w, XMLEventFactory ef, EventComponentFactory ecf, ExpressionParser eParser, EvaluationContext eContext) {
        reader         = r;
        writer         = w;
        elementFactory = ef;
        eComponentFactory = ecf;
        expressionParser  = eParser;
        evaluationContext = eContext;
        
        globalQueue    = new LinkedList<XMLEvent>();
        eventQueue     = new LinkedList<XMLEvent>();
        env            = new LinkedList<EventEnvironmentImpl>();
        actionRegistry = new ActionRegistry();
        
        env.push(new EventEnvironmentImpl());
    }

    public boolean hasNextEvent(){
        return globalQueue.size() > 0 || reader.hasNext();
    }
    
    public XMLEvent nextEvent() throws XMLStreamException {
        XMLEvent e;
        if(globalQueue.size() > 0) {
            e = globalQueue.poll();
        } else {
            e = reader.nextEvent();
        }
        
        if(env.peek().getInnerCount() < 0) {
            endEnvironment();
        }

        if(e.isStartElement()) {
            env.peek().incInnerCount();
        } else if(e.isEndElement()) {
            env.peek().decInnerCount();
        }
        
        //System.out.println("Next event: " + e);
        return e;
    }
    
    public XMLEvent peekEvent() throws XMLStreamException {
        if(globalQueue.size() > 0) return globalQueue.peek();
        return reader.peek();
    }
    
    public void queueEvent(XMLEvent e) {
        //System.out.println("Queuing: " + e);
        eventQueue.offer(e);
    }
    
    public void flushEventQueue() {
        if(!eventQueue.isEmpty()) {
            //System.out.println("Flushing queue");
            //System.out.println("  Event queue: " + eventQueue);
            //System.out.println("  Global queue: " + globalQueue);
            while(!eventQueue.isEmpty()) {
                XMLEvent e = eventQueue.removeLast();
                globalQueue.addFirst(e);
            }
            //System.out.println("  Global queue after: " + globalQueue);
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
    
    public EventComponentFactory getComponentFactory() {
        return eComponentFactory;
    }

    public EvaluationContext getEvaluationContext(){
        return evaluationContext;
    }
    
    public <T> T parseExpression(String expression, Class<T> clazz) {
        Expression expr = expressionParser.parseExpression(expression);
        return expr.getValue(getEvaluationContext(), clazz);
    }
    
    public <T> T parseExpression(String expression, Object rootObject, Class<T> clazz) {
        Expression expr = expressionParser.parseExpression(expression);
        return expr.getValue(getEvaluationContext(), rootObject, clazz);
    }
    
    public <T> T parseTemplateExpression(String expression, Class<T> clazz) {
        Expression expr = expressionParser.parseExpression(expression, TEMPLATE_PARSER_CONTEXT);
        return expr.getValue(getEvaluationContext(), clazz);
    }
    
    public void setExpressionValue(String expression, Object value) {
        Expression expr = expressionParser.parseExpression(expression);
        expr.setValue(getEvaluationContext(), value);
    }

    public EventEnvironmentImpl getEventEnvironment() {
        EventEnvironmentImpl e = env.peek();
        if(e.getInnerCount() > 0) {
            env.push(new EventEnvironmentImpl(e));
            e = env.peek();
        }
        return e;
    }
    
    private void endEnvironment() {
        env.pop();
    }

}
