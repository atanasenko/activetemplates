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

package org.sleepless.at.impl.handlers;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.sleepless.at.events.AttributeHandler;
import org.sleepless.at.events.ElementHandler;
import org.sleepless.at.lib.elements.If;
import org.sleepless.at.spi.HandlerSPI;

public class StandardHandlerSPI implements HandlerSPI {
    
    public static final Map<QName, AttributeHandler> attributes = new HashMap<QName, AttributeHandler>();
    public static final Map<QName, ElementHandler> elements = new HashMap<QName, ElementHandler>();
    
    static {
        
        elements.put(If.TAG, new If());
        
    }

    @Override
    public Map<QName, AttributeHandler> getAttributeHandlers() {
        return attributes;
    }

    @Override
    public Map<QName, ElementHandler> getElementHandlers() {
        return elements;
    }

}
