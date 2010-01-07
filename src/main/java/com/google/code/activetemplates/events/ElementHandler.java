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
 * Handler of start/end element event pair
 * 
 * @author sleepless
 *
 */
public interface ElementHandler {
    
    /**
     * Outcome of element processing
     * 
     * @author sleepless
     */
    public enum Outcome {
        
        /**
         * Continue with processing current element children
         * 
         * May only be returned from processStart
         */
        PROCESS_CHILDREN,
        
        /**
         * Continue with processing current element's children.
         * 
         * Skips any children if returned from processStart
         */
        PROCESS_SIBLINGS;
    }
    
    /**
     * Gets an array of QNames this handler can handle
     * @return
     */
    public QName[] getElements();
    
    /**
     * Processes start element.
     * Possible outcomes: PROCESS_CHILDREN, PROCESS_SIBLINGS
     * 
     * @param e
     * @return
     */
    public Outcome processStart(StartElementEvent e) throws XMLStreamException;
    
    /**
     * Processes end element.
     * 
     * @param e
     * @return
     */
    public void processEnd(EndElementEvent e) throws XMLStreamException;
    
}
