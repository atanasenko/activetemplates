package com.google.code.activetemplates.lib.elements;

import java.util.Queue;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import com.google.code.activetemplates.EventStream;
import com.google.code.activetemplates.events.ElementHandler;
import com.google.code.activetemplates.events.EndElementEvent;
import com.google.code.activetemplates.events.StartElementEvent;
import com.google.code.activetemplates.events.TemplateEvent;
import com.google.code.activetemplates.impl.handlers.BuiltinHandlerSPI;

public class BodyEl implements ElementHandler {
    
    public static final QName ELEMENT = new QName(
            BuiltinHandlerSPI.NAMESPACE_STDLIB, "body");

    private static final String BODY_SCOPE = BodyEl.class.getName() + ".scope";

    public QName[] getElements() {
        return new QName[]{ ELEMENT };
    }

    @Override
    public Outcome processStart(StartElementEvent e) throws XMLStreamException {
        BodyScope bScope = e.getEnvironment().get(BODY_SCOPE, BodyScope.class);
        if(bScope == null) {
            throw new IllegalArgumentException("No body scope found");
        } else if(bScope.drained) {
            throw new IllegalArgumentException("Body already drained");
        }
        bScope.drain(e.getEventStream());
        
        return null;
    }
    
    @Override
    public void processEnd(EndElementEvent e) throws XMLStreamException {
    }

    public static void setBody(TemplateEvent e, Queue<XMLEvent> body) {
        e.getEnvironment().put(BODY_SCOPE, new BodyScope(body));
    }
    
    private static class BodyScope {
        
        boolean drained;
        Queue<XMLEvent> body;
        
        BodyScope(Queue<XMLEvent> body) {
            drained = false;
            this.body = body;
        }
        
        void drain(EventStream stream) {
            for(XMLEvent e: body) {
                stream.queueEvent(e);
            }
            drained = true;
        }
        
    }

}
