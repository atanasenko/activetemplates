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

import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.code.activetemplates.Template;
import com.google.code.activetemplates.TemplateCompileException;
import com.google.code.activetemplates.TemplateCompiler;
import com.google.code.activetemplates.events.AttributeHandler;
import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.spi.HandlerSPI;
import com.google.code.activetemplates.spi.Providers;

public class TemplateCompilerImpl implements TemplateCompiler {
    
    private XMLOutputFactory outFactory;
    private XMLInputFactory inFactory;
    private XMLEventFactory eFactory;
    private Handlers h;
    
    private Set<String> excludedNamespaces;
    private ExpressionParser expressionParser;
    
    public TemplateCompilerImpl(){

        outFactory = XMLOutputFactory.newInstance();
        inFactory = XMLInputFactory.newInstance();
        eFactory = XMLEventFactory.newInstance();
        
        h = new Handlers();
        
        excludedNamespaces = new HashSet<String>();
        List<HandlerSPI> spis = Providers.getHandlerSPIs();
        
        for(HandlerSPI spi: spis) {
            Set<String> s = spi.getExcludedNamespaces();
            if(s != null) {
                excludedNamespaces.addAll(s);
            }
        }
        
        expressionParser = new SpelExpressionParser();
    }

    @Override
    public void compile(Template t, Object context, OutputStream out) throws TemplateCompileException {
        compile(t, context, new StreamResult(out));
    }

    @Override
    public void compile(Template t, Object context, Writer out) throws TemplateCompileException {
        compile(t, context, new StreamResult(out));
    }

    @Override
    public void compile(final Template t, final Object context, Result out) throws TemplateCompileException {
        
        final TemplateImpl ti = (TemplateImpl) t;
        
        final Source s = t.createSource();
        XMLEventReader r = null;
        XMLEventWriter w = null;
        try {
            r = inFactory.createXMLEventReader(s);
            w = outFactory.createXMLEventWriter(out);
            final XMLStreamException[] xe = new XMLStreamException[1];

            final XMLEventReader fr = r;
            final XMLEventWriter fw = w;
            
            EvaluationContext eContext = new StandardEvaluationContext(context);
                        
            CompileContext ctx = new CompileContext(fr, fw, eFactory, expressionParser, eContext);
            doCompile(t.getName(), ctx);
            
            if(xe[0] != null) {
                throw xe[0];
            }

        } catch(XMLStreamException e) {
            throw new TemplateCompileException(e);
        } finally {
            ti.getXmlCache().close(s);
            if(r != null) try{ r.close(); } catch(XMLStreamException e){}
            if(w != null) try{ w.close(); } catch(XMLStreamException e){}
        }
    }
    
    private void doCompile(String name, CompileContext cc) throws XMLStreamException {
        
        while(cc.hasNextEvent()) {

            XMLEvent e = cc.nextEvent();

            //Location loc = e.getLocation();
            
            if_tag:
            if(e.isStartElement()) {
                
                StartElement se = e.asStartElement();
                
                boolean replaceElement = false;
                
                @SuppressWarnings("unchecked")
                Iterator<Namespace> nsit = se.getNamespaces();
                List<Namespace> namespaces = new ArrayList<Namespace>();
                
                while(nsit.hasNext()) {
                    Namespace ns = nsit.next();
                    if(excludedNamespaces.contains(ns.getNamespaceURI())) {
                        replaceElement = true;
                    } else {
                        namespaces.add(ns);
                    }
                }
                
                @SuppressWarnings("unchecked")
                Iterator<Attribute> it = se.getAttributes();
                List<Attribute> attributes = new ArrayList<Attribute>();
                
                // preprocess attributes
                cycle_attr:
                while(it.hasNext()) {
                    Attribute a = it.next();
                    
                    String value = a.getValue();
                    String nvalue = processText(cc, value);
                    if(nvalue != null) {
                        a = cc.getElementFactory().createAttribute(a.getName(), nvalue);
                        replaceElement = true;
                    }
                    
                    if(h.isAttributeHandled(a.getName())) {

                        replaceElement = true;
                        AttributeHandler.Outcome o = h.processAttribute(cc, a);
                        
                        switch(o) {
                        case PROCESS_TAG:
                            break cycle_attr;
                            
                        case PROCESS_NONE:
                            skipChildren(cc, false);
                            break if_tag;
                        }
                        
                    } else {
                        attributes.add(a);
                    }
                }
                
                if(replaceElement) {
                    // replace element with new one
                    se = cc.getElementFactory()
                            .createStartElement(
                                    se.getName(), 
                                    attributes.iterator(), 
                                    namespaces.iterator());
                }
                
                // handle start element
                if(h.isElementHandled(se.getName())) {
                    ElementHandler.Outcome o = h.processStartElement(cc, se);
                    cc.flushEventQueue();
                    switch(o){ 
                    case PROCESS_SIBLINGS:
                        skipChildren(cc, true);
                        break;
                    }
                } else {
                    cc.getWriter().add(se);
                    cc.flushEventQueue(); // flush events added by any attribute handlers
                }
                
            } else if(e.isEndElement()) {
                
                // handle end element
                if(h.isElementHandled(e.asEndElement().getName())) {
                    ElementHandler.Outcome o = h.processEndElement(cc, e.asEndElement());
                    cc.flushEventQueue();

                    switch(o){ 
                    case PROCESS_PARENT:
                        skipSiblings(cc);
                        break;
                    
                    case PROCESS_SIBLINGS:
                        break;
                    }
                } else {
                    cc.getWriter().add(e);
                }
                
            } else if(e.isCharacters()) {
                
                // process text
                Characters ce = e.asCharacters();
                String s = ce.getData();
                String ns = processText(cc, s);
                if(ns != null) {
                    ce = cc.getElementFactory().createCharacters(ns);
                }
                cc.getWriter().add(ce);
                
            }
            
        }
        
    }
    
    private String processText(CompileContext cc, String data) {
        String val = cc.parseTemplateExpression(data, String.class);
        if(val == null) val = "";
        if(val.equals(data))
            return null;
        
        return val;
    }
    
    // skip all elements until current tag's end is encountered
    private static void skipChildren(CompileContext cc, boolean processEnd) throws XMLStreamException {
        skipElements(cc, 1, processEnd);
    }

    // skip all elements until parent tag's end is encountered
    private static void skipSiblings(CompileContext cc) throws XMLStreamException {
        skipElements(cc, 2, true);
    }
    
    // skip elements until level reaches 0
    private static void skipElements(CompileContext cc, int initialLevel, boolean processEnd) throws XMLStreamException {
        
        while(cc.hasNextEvent()) {
            XMLEvent e = cc.peekEvent();
            
            if(e.isStartElement()) {
                initialLevel++;
            } else if(e.isEndElement()) {
                initialLevel--;
            }
            
            if(initialLevel == 0) {
                // do not remove the event if we need to process it later
                if(!processEnd) cc.nextEvent();
                break;
            }
            cc.nextEvent();
            
        }
        
    }
    
}
