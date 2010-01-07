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

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

/**
 * Attribute event handler
 * 
 * @author sleepless
 *
 */
public interface AttributeHandler {

    /**
     * Defines the outcome of attribute processing
     * 
     * @author sleepless
     *
     */
    public enum Outcome {
        
        /**
         * Default outcome, processing will continue with next attribute
         */
        PROCESS_ALL,
        
        /**
         * Skips processing the tag and its children.
         * Tags start element handler will not be called
         */
        PROCESS_NONE;
    }
    
    /**
     * Gets an array of attribute names this handler can handle
     * @return
     */
    public QName[] getAttributes();
    
    /**
     * Process attribute event.
     * Called after start element was read from eventReader and before
     * its processing commences.
     * 
     * @param attr
     * @return
     */
    public Outcome processAttribute(AttributeEvent attr) throws XMLStreamException;
    
    
}
