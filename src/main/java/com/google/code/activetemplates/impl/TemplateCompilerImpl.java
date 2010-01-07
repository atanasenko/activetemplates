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
import java.util.LinkedList;
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
import javax.xml.transform.stream.StreamResult;

import org.codehaus.stax2.XMLOutputFactory2;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.code.activetemplates.Template;
import com.google.code.activetemplates.TemplateCompileException;
import com.google.code.activetemplates.TemplateCompiler;
import com.google.code.activetemplates.TemplateModel;
import com.google.code.activetemplates.events.AttributeHandler;
import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.AttributeHandler.Outcome;
import com.google.code.activetemplates.spi.HandlerSPI;
import com.google.code.activetemplates.spi.Providers;
import com.google.code.activetemplates.xml.XmlResult;
import com.google.code.activetemplates.xml.XmlSource;
import com.google.code.activetemplates.xml.XmlStreamResult;

public class TemplateCompilerImpl implements TemplateCompiler {
    
    private XMLOutputFactory outFactory;
    private XMLInputFactory inFactory;
    private XMLEventFactory eFactory;
    private EventComponentFactory eComponentFactory;
    
    private Set<String> excludedNamespaces;
    private ExpressionParser expressionParser;

    private Handlers h;
    
    public TemplateCompilerImpl(){

        outFactory = XMLOutputFactory.newInstance();
        outFactory.setProperty(XMLOutputFactory2.IS_REPAIRING_NAMESPACES, true);
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
        
        eComponentFactory = new EventComponentFactory();
        expressionParser = new SpelExpressionParser();
    }
    
    

    @Override
    public void compile(Template t, TemplateModel model, OutputStream out) throws TemplateCompileException {
        compile(t, model, new XmlStreamResult(new StreamResult(out)));
    }

    @Override
    public void compile(Template t, TemplateModel model, Writer out) throws TemplateCompileException {
        compile(t, model, new XmlStreamResult(new StreamResult(out)));
    }

    @Override
    public void compile(final Template t, final TemplateModel model, XmlResult out) throws TemplateCompileException {
        
        final XmlSource s = t.createSource();
        XMLEventReader r = null;
        XMLEventWriter w = null;
        try {
            r = inFactory.createXMLEventReader(s.getSource());
            w = outFactory.createXMLEventWriter(out.getResult());
            final XMLStreamException[] xe = new XMLStreamException[1];

            final XMLEventReader fr = r;
            final XMLEventWriter fw = w;
            
            StandardEvaluationContext eContext = new StandardEvaluationContext(model);
            eContext.addPropertyAccessor(new TemplateModelPropertyAccessor());
                        
            CompileContext ctx = new CompileContext(fr, fw, 
                        eFactory, eComponentFactory, expressionParser, eContext);
            doCompile(t.getName(), ctx);
            
            if(xe[0] != null) {
                throw xe[0];
            }

        } catch(XMLStreamException e) {
            throw new TemplateCompileException(e);
        } finally {
            s.close();
            if(r != null) try{ r.close(); } catch(XMLStreamException e){}
            if(w != null) try{ w.close(); } catch(XMLStreamException e){}
        }
    }
    
    private void doCompile(String name, CompileContext cc) throws XMLStreamException {
        
        while(cc.hasNextEvent()) {

            XMLEvent e = cc.nextEvent();

            //Location loc = e.getLocation();
            
            if(e.isAttribute()) {
                //System.out.println("Adding " + e);
                
                // attributes added during tag processing and under the same tag
                // get handled here, outcome is always PROCESS_ALL
                
                Attribute a = (Attribute) e;
                if(h.isAttributeHandled(a.getName())) {
                    h.processAttribute(cc, a);
                } else {
                    String value = a.getValue();
                    String nvalue = processText(cc, value);
                    if(nvalue != null) {
                        a = cc.getElementFactory().createAttribute(a.getName(), nvalue);
                    }
                    //System.out.println("Adding " + e);
                    cc.getWriter().add(a);
                }                
                
            } else if(e.isStartElement()) {
                
                StartElement se = e.asStartElement();
                
                Processing processing = Processing.DEFAULT;
                
                // collect namespaces
                @SuppressWarnings("unchecked")
                Iterator<Namespace> nsit = se.getNamespaces();
                List<Namespace> namespaces = new ArrayList<Namespace>();
                
                while(nsit.hasNext()) {
                    Namespace ns = nsit.next();
                    if(excludedNamespaces.contains(ns.getNamespaceURI())) {
                        processing = Processing.REPLACE;
                    } else {
                        namespaces.add(ns);
                    }
                }
                
                // collect attributes
                @SuppressWarnings("unchecked")
                Iterator<Attribute> it = se.getAttributes();
                List<Attribute> attributes = new LinkedList<Attribute>();
                while(it.hasNext()) {
                    attributes.add(it.next());
                }
                
                // collect any separate attribute and namespace xml events
                while(cc.hasNextEvent()) {
                    if(cc.peekEvent().isNamespace()) {
                        namespaces.add((Namespace)cc.nextEvent());
                        processing = Processing.REPLACE;
                    } else if(cc.peekEvent().isAttribute()) {
                        attributes.add((Attribute)cc.nextEvent());
                        processing = Processing.REPLACE;
                    } else {
                        break;
                    }
                }
                
                // preprocess attributes
                it = attributes.iterator();
                attributes = new ArrayList<Attribute>();
                
                while(it.hasNext() && processing != Processing.SKIP) {
                    Attribute a = it.next();
                    
                    if(h.isAttributeHandled(a.getName())) {
                        processing = Processing.REPLACE;

                        AttributeHandler.Outcome o = h.processAttribute(cc, a);
                        if(o == Outcome.PROCESS_NONE) {
                            processing = Processing.SKIP;
                        }
                        
                    } else {
                        String value = a.getValue();
                        String nvalue = processText(cc, value);
                        if(nvalue != null) {
                            a = cc.getElementFactory().createAttribute(a.getName(), nvalue);
                            processing = Processing.REPLACE;
                        }
                        
                        attributes.add(a);
                    }
                }

                if(processing == Processing.SKIP) {
                    
                    skipChildren(cc, false);
                    
                } else {
                
                    if(processing == Processing.REPLACE) {
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
                        //System.out.println("Adding " + se);
                        cc.getWriter().add(se);
                        cc.flushEventQueue(); // flush events added by any attribute handlers
                    }
                }
                
            } else if(e.isEndElement()) {
                
                // handle end element
                if(h.isElementHandled(e.asEndElement().getName())) {
                    h.processEndElement(cc, e.asEndElement());
                    cc.flushEventQueue();
                } else {
                    //System.out.println("Adding " + e);
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
                //System.out.println("Adding " + e);
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
    
    private enum Processing {
        SKIP,
        REPLACE,
        DEFAULT;
    }
    
}
