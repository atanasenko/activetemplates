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

/**
 * Contract for attribute handlers.
 * 
 * Attribute handling is done twice: 
 * 1. preProcessAttribute method is called before start element handler is 
 * called and one can decide over the outcome of the processing.
 * 
 * 2. postProcessAttribute method is called after start element handler was
 * called
 * 
 * @author sleepless
 *
 */
public interface AttributeHandler {

    /**
     * Defines the outcome of attribute preprocessing
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
         * Skips processing remaining attributes
         */
        PROCESS_TAG,
        
        /**
         * Skips processing the tag and its children
         */
        PROCESS_NONE;
    }
    
    /**
     * Preprocess phase of attribute event.
     * Called after start element was read from eventReader and before
     * its processing commences.
     * 
     * @param attr
     * @return
     */
    public Outcome preProcessAttribute(AttributeEvent attr);
    
    /**
     * Postprocess phase of attribute event.
     * Called after start element processing finishes
     * 
     * @param attr
     */
    public void postProcessAttribute(AttributeEvent attr);
    
}
