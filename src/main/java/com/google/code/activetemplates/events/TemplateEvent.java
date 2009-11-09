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

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.events.XMLEvent;

import com.google.code.activetemplates.bind.Bindings;
import com.google.code.activetemplates.script.ScriptingProvider;

public interface TemplateEvent {

    public XMLEvent getEvent();
    
    public XMLEventWriter getEventWriter();
    
    public XMLEventReader getEventReader();
    
    public XMLEventFactory getElementFactory();
    
    public ScriptingProvider getScriptingProvider();
    
    public Bindings getBindings();
    
}
