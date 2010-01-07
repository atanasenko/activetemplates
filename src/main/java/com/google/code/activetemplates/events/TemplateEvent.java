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

import javax.xml.stream.events.XMLEvent;

import com.google.code.activetemplates.EventStream;

/**
 * Base interface for all template events.
 * 
 * Note that TemplateEvent instances might be cached internally, so they should
 * not be used outside of corresponding process method.
 * 
 * @author sleepless
 * 
 */
public interface TemplateEvent {

    /**
     * Returns xml event associated with the current template event
     * 
     * @return
     */
    public XMLEvent getEvent();

    /**
     * Parses expression using current evaluation context
     * @param <T>
     * @param expression
     * @param clazz
     * @return
     */
    public <T> T parseExpression(String expression, Class<T> clazz);
    
    /**
     * Parses template expression using current evaluation context
     * @param <T>
     * @param expression
     * @param clazz
     * @return
     */
    public <T> T parseTemplateExpression(String expression, Class<T> clazz);

    /**
     * Parses expression using current evaluation context and specified root object
     * @param <T>
     * @param expression
     * @param rootObject
     * @param clazz
     * @return
     */
    public <T> T parseExpression(String expression, Object rootObject, Class<T> clazz);

    /**
     * Set expression value
     * @param expression
     * @param value
     */
    public void setExpressionValue(String expression, Object value);

    /**
     * Executes action identified by specified action id
     * 
     * @param aid
     */
    public void executeAction(String aid);

    /**
     * Gets the associated event stream
     * @return
     */
    public EventStream getEventStream();
    
    /**
     * Gets current event environment
     * @return
     */
    public EventEnvironment getEnvironment();
    
}
