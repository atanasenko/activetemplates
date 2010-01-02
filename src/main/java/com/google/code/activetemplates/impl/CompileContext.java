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

public class CompileContext {
    
    private static final ParserContext TEMPLATE_PARSER_CONTEXT = 
        new TemplateParserContext("${", "}");

    private XMLEventReader reader;
    private XMLEventWriter writer;
    private XMLEventFactory elementFactory;
    
    private Deque<XMLEvent> globalQueue;
    private Deque<XMLEvent> eventQueue;
    private ActionRegistry actionRegistry;
    
    private ExpressionParser expressionParser;
    private EvaluationContext evaluationContext;
    
    public CompileContext(XMLEventReader r, XMLEventWriter w, XMLEventFactory ef, ExpressionParser eParser, EvaluationContext eContext) {
        reader         = r;
        writer         = w;
        elementFactory = ef;
        expressionParser  = eParser;
        evaluationContext = eContext;
        
        globalQueue    = new LinkedList<XMLEvent>();
        eventQueue     = new LinkedList<XMLEvent>();
        actionRegistry = new ActionRegistry();
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

    public EvaluationContext getEvaluationContext(){
        return evaluationContext;
    }
    
    public <T> T parseExpression(String expression, Class<T> clazz) {
        Expression expr = expressionParser.parseExpression(expression);
        return expr.getValue(getEvaluationContext(), clazz);
    }
    
    public <T> T parseTemplateExpression(String expression, Class<T> clazz) {
        Expression expr = expressionParser.parseExpression(expression, TEMPLATE_PARSER_CONTEXT);
        return expr.getValue(getEvaluationContext(), clazz);
    }
    
    public void setExpressionValue(String expression, Object value) {
        Expression expr = expressionParser.parseExpression(expression);
        expr.setValue(getEvaluationContext(), value);
    }

}
