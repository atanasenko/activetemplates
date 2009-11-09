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

package com.google.code.activetemplates;

import java.io.OutputStream;
import java.io.Writer;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Result;

import com.google.code.activetemplates.bind.Bindings;

/**
 * Template compiler takes a template and compiles it into target document
 * using provided bindings.
 * 
 * @author sleepless
 *
 */
public interface TemplateCompiler {
    
    /**
     * Compiles template into specified outputStream.
     * 
     * @param t
     * @param b
     * @param out
     * @throws XMLStreamException
     */
    public void compile(Template t, Bindings b, OutputStream out) throws XMLStreamException;

    /**
     * Compiles template into specified reader.
     * Compile method which takes OutputStream is preferrable to this one.
     * 
     * @param t
     * @param b
     * @param out
     * @throws XMLStreamException
     */
    public void compile(Template t, Bindings b, Writer out) throws XMLStreamException;

    /**
     * Compiles template into specified xml Result.
     * 
     * @param t
     * @param b
     * @param out
     * @throws XMLStreamException
     */
    public void compile(Template t, Bindings b, Result out) throws XMLStreamException;
    
    /**
     * Creates a new Bindings object which can be populated with data and passed
     * to the compile method.
     * @return
     */
    public Bindings createBindings();
    
}
