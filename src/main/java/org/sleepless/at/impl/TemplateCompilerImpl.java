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

package org.sleepless.at.impl;

import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import org.sleepless.at.Template;
import org.sleepless.at.TemplateCompiler;
import org.sleepless.at.events.AttributeHandler;
import org.sleepless.at.events.ElementHandler;

public class TemplateCompilerImpl implements TemplateCompiler {
    
    private XMLOutputFactory outFactory;
    private XMLInputFactory inFactory;
    private XMLEventFactory eFactory;
    private Handlers h;
    
    public TemplateCompilerImpl(){
        outFactory = XMLOutputFactory.newInstance();
        inFactory = XMLInputFactory.newInstance();
        eFactory = XMLEventFactory.newInstance();
        
        h = new Handlers();
    }

    @Override
    public void compile(Template t, OutputStream out) throws XMLStreamException {
        compile(t, new StreamResult(out));
    }

    @Override
    public void compile(Template t, Writer out) throws XMLStreamException {
        compile(t, new StreamResult(out));
    }

    @Override
    public void compile(Template t, Result out) throws XMLStreamException {
        
        TemplateImpl ti = (TemplateImpl) t;
        
        Source s = t.getSource();
        XMLEventWriter w = outFactory.createXMLEventWriter(out);
        XMLEventReader r = inFactory.createXMLEventReader(s);
        
        try {
            
            CompileContext ctx = new CompileContext(r, w, eFactory);
            
            doCompile(ctx);
            
        } finally {
            ti.getXmlCache().close(s);
            w.close();
            r.close();
        }
        
    }
    
    private void doCompile(CompileContext cc) throws XMLStreamException {
        
        while(cc.getReader().hasNext()) {
            
            XMLEvent e = cc.getEventQueue().poll();
            
            if(e == null) {
                e = cc.getReader().nextEvent();
            }
            
            if_tag:
            if(e.isStartElement()) {
                
                StartElement se = e.asStartElement();
                
                @SuppressWarnings("unchecked")
                Iterator<Attribute> it = se.getAttributes();
                
                boolean attributesHandled = false;

                List<Attribute> processed = new ArrayList<Attribute>();
                List<Attribute> unprocessed = new ArrayList<Attribute>();
                
                // preprocess attributes
                cycle_attr:
                while(it.hasNext()) {
                    Attribute a = it.next();
                    
                    String value = a.getValue();
                    String nvalue = processText(cc, value);
                    if(nvalue != null) {
                        a = cc.getElementFactory().createAttribute(a.getName(), nvalue);
                        attributesHandled = true;
                    }
                    
                    if(h.isAttributeHandled(a.getName())) {
                        processed.add(a);

                        attributesHandled = true;
                        AttributeHandler.Outcome o = h.preProcessAttribute(cc, a);
                        
                        switch(o) {
                        case PROCESS_TAG:
                            break cycle_attr;
                            
                        case PROCESS_NONE:
                            skipChildren(cc, false);
                            break if_tag;
                            
                        case PROCESS_ATTRIBUTES:
                            break;
                        }
                        
                    } else {
                        unprocessed.add(a);
                    }
                }
                
                if(attributesHandled) {
                    se = cc.getElementFactory().createStartElement(se.getName(), unprocessed.iterator(), se.getNamespaces());
                }
                
                // handle start element
                if(h.isElementHandled(e.asStartElement().getName())) {
                    ElementHandler.Outcome o = h.processStartElement(cc, e.asStartElement());
                    
                    switch(o){ 
                    case PROCESS_SIBLINGS:
                        skipChildren(cc, false);
                        break;

                    case PROCESS_CHILDREN:
                        break;
                    }
                } else {
                    cc.getWriter().add(e);
                }
                
                // postprocess attributes
                for(Attribute a: processed) {
                    
                    h.postProcessAttribute(cc, a);
                    
                }
                
                
            } else if(e.isEndElement()) {
                
                // handle end element
                if(h.isElementHandled(e.asEndElement().getName())) {
                    ElementHandler.Outcome o = h.processEndElement(cc, e.asEndElement());

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
        // TODO
        return null;
    }
    
    // skip all elements until current tag's end is encountered
    private static void skipChildren(CompileContext cc, boolean processEnd) throws XMLStreamException {
        
        int num = 1;
        
        while(cc.getReader().hasNext()) {
            XMLEvent e = cc.getReader().nextEvent();
            
            if(e.isStartElement()) {
                num++;
            } else if(e.isEndElement()) {
                num--;
            }
            
            if(num == 0) {
                // queue end node to handle it later
                if(processEnd) cc.getEventQueue().offer(e);
                break;
            }
            
        }
        
    }
    
    // skip all elements unitl parent tag's end is encountered
    private static void skipSiblings(CompileContext cc) throws XMLStreamException {
        
        int num = 2;

        while(cc.getReader().hasNext()) {
            XMLEvent e = cc.getReader().nextEvent();
            
            if(e.isStartElement()) {
                num++;
            } else if(e.isEndElement()) {
                num--;
            }
            
            if(num == 0) {
                // queue end node to handle it later
                cc.getEventQueue().offer(e);
                break;
            }
            
        }
        
        
    }

}
