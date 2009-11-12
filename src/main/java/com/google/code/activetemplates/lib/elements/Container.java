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

package com.google.code.activetemplates.lib.elements;

import javax.xml.namespace.QName;

import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.StartElementEvent;
import com.google.code.activetemplates.impl.handlers.StandardHandlerSPI;

/**
 * Element which start and end tags will be omitted from output.
 * 
 * Useful when an included template in a separate xml file doesn't have a 
 * single root element.
 * 
 * @author sleepless
 *
 */
public class Container implements ElementHandler {

    public static final QName TAG = new QName(StandardHandlerSPI.NAMESPACE_STDLIB, "container");

    public Outcome processStart(StartElementEvent e) {
        return Outcome.PROCESS_CHILDREN;
    }

    public Outcome processEnd(EndElementEvent e) {
        return Outcome.PROCESS_SIBLINGS;
    }

}
